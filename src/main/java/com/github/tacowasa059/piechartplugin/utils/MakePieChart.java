package com.github.tacowasa059.piechartplugin.utils;

import com.github.tacowasa059.piechartplugin.PieChartPlugin;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
//画像処理クラス
public class MakePieChart {
    private final PieChartPlugin plugin;
    public MakePieChart(PieChartPlugin plugin){
        this.plugin=plugin;
    }
    //②Listに基づき、pie_chartの作成
    public BufferedImage getPieChart(List<Map.Entry<Integer, Integer>> list, String chartFile){
        DefaultPieDataset ds_pie = new DefaultPieDataset();
        for(Map.Entry<Integer,Integer> entry:list){
            ds_pie.setValue(entry.getKey(),entry.getValue());
        }
        //レガシーテーマを設定する
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
        //円グラフ作成
        JFreeChart chart= ChartFactory.createPieChart("color", ds_pie,false,false,true);
        //JFreeChart chart = new JFreeChart(new PiePlot(ds_pie));
        //chart.setBorderVisible(false);
        //chart.setBackgroundPaint( null );
        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelGenerator(null);
        //色の設定
        for(Map.Entry<Integer,Integer> entry:list){
            int color=entry.getKey();
            int r = ( color >> 16 ) & 0xff;
            int g = ( color >> 8 ) & 0xff;
            int b = color & 0xff;
            plot.setSectionPaint(color, new Color(r, g, b));
        }

        //bufferedimageとして保存
        BufferedImage chartImage = chart.createBufferedImage(128*9, 128*9);
        chartImage=chartImage.getSubimage(128,128,128*7,128*7);
        if(plugin.getConfig().getBoolean("SaveImageAsPNG")){
            try {
                ImageIO.write(chartImage, "png", new File(plugin.getDataFolder(),chartFile));

            } catch (IOException e) {
                throw new IllegalArgumentException("Failed writing chartFile (" + chartFile + ").", e);
            }
        }
        return chartImage;
    }
    //色のカウントを行う(①)
    public static List<Map.Entry<Integer, Integer>> CountColor(BufferedImage img){
        Map<Integer,Integer> map =new HashMap<>();//カウント用のmap
        for(int x=0;x<img.getWidth();x++){
            for(int y=0;y<img.getHeight();y++){
                int color=img.getRGB(x,y);
                int a = color >> 24;
                if(a!=0){
                    if(map.containsKey(color)){
                        map.put(color,map.get(color)+1);
                    }
                    else{
                        map.put(color,1);
                    }
                }
            }
        }
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());//並び替え
        Collections.reverse(list);//逆順
        return list;
    }
    //似た色をまとめる用
    public List<Map.Entry<Integer,Integer>> modify_image(List<Map.Entry<Integer,Integer>> list){
        Map<Integer,Integer> list_modified=new HashMap<>();
        for(Map.Entry<Integer,Integer> entry_list:list){
            boolean flag=false;
            int color=entry_list.getKey();
            int r = ( color >> 16 ) & 0xff;
            int g = ( color >> 8 ) & 0xff;
            int b = color & 0xff;
            for(Map.Entry<Integer,Integer> entry_list_modified:list_modified.entrySet()){
                int color_modified=entry_list_modified.getKey();
                int r_modified = ( color_modified >> 16 ) & 0xff;
                int g_modified = ( color_modified >> 8 ) & 0xff;
                int b_modified = color_modified & 0xff;
                double distance=Math.sqrt((double)(r-r_modified)*(r-r_modified)+(double)(g-g_modified)*(g-g_modified)+(double)(b-b_modified)*(b-b_modified));
                if(distance<plugin.getConfig().getDouble("ThresholdDistance")){
                    entry_list_modified.setValue(entry_list.getValue()+entry_list_modified.getValue());
                    flag=true;
                    break;
                }
            }
            if(!flag){
                list_modified.put(color, entry_list.getValue());
            }
        }
        List<Map.Entry<Integer, Integer>> list2 = new ArrayList<>(list_modified.entrySet());
        list2.sort(Map.Entry.comparingByValue());//並び替え
        Collections.reverse(list2);//逆順
        return list2;
    }
    //sort color
    public List<Map.Entry<Integer,Integer>> sort_color_count(List<Map.Entry<Integer,Integer>> List){
        List<Map.Entry<Integer,Integer>> List2=new ArrayList<>(List);
        int i=0;
        while(i<List2.size()-1){
            int insert_index=i+1;
            int j=i+1;
            while(j<List2.size()){
                int color1=List2.get(i).getKey();
                int r1 = ( color1 >> 16 ) & 0xff;
                int g1 = ( color1 >> 8 ) & 0xff;
                int b1 = color1 & 0xff;
                int color2=List2.get(j).getKey();
                int r2 = ( color2 >> 16 ) & 0xff;
                int g2 = ( color2 >> 8 ) & 0xff;
                int b2 = color2 & 0xff;
                double norm=Math.sqrt((double)((r1-r2)*(r1-r2)+(g1-g2)*(g1-g2)+(b1-b2)*(b1-b2)));
                if(norm<plugin.getConfig().getDouble("ThresholdDistance")){
                    int count2=List2.get(j).getValue();
                    List2.remove(j);
                    HashMap<Integer,Integer> hashMap=new HashMap<>();
                    hashMap.put(color2,count2);
                    for(Map.Entry<Integer,Integer> entry:hashMap.entrySet()){
                        List2.add(insert_index,entry);
                    }
                    insert_index+=1;
                }
                j+=1;
            }
            i+=insert_index-i;
        }
        return List2;
        /*
        while(i<len(original_color)):
        insert_index=i+1#insertの位置
                j=i+1
        while(j<len(original_color)):
        norm=np.linalg.norm(np.array(original_color[i])-np.array(original_color[j]),ord=2)
        if norm<40.0/255.0:
        color=original_color.pop(j)
        original_color.insert(insert_index,color)
        count=original_count.pop(j)
        original_count.insert(insert_index,count)
        insert_index+=1
        j+=1
        i+=insert_index-i

         */
    }

}
