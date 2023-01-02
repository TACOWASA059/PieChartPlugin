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
                ImageIO.write(chartImage, "png", new File(chartFile));
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed writing chartFile (" + chartFile + ").", e);
            }
        }
        return chartImage;
    }
    //色のカウントを行う
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


}
