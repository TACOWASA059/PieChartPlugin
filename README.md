# PieChartPlugin
スキンの色割合を円グラフで表示するプラグイン
## コマンド
- /piechart get MCID
### 注意
地図にすると配色が変わるため微妙

### 内部処理
1. マイクラサーバーからスキン画像を取得
1. 円グラフを作成
1. 地図を作成

### 円グラフ種類
- original : 色を降順に並び変えたもの
- clustered : 類似色を纏めたグラフ(originalと色の比率が変わる)
- sorted : 類似色が並ぶように、originalを並び替えたグラフ

## コンフィグ
| 変数 | 説明 | 
| ----- | ----- |
|ThresholdDistance |類似色の判定に用いる閾値 (RGBに対する二乗誤差の平方根)|
|SaveImageAsPNG| PNGファイルとして保存するかどうか (保存先はplugin/PieChartPlugin/以下)

- /piechart setThreshold value : ThresholdDistanceの設定
- /piechart showConfig : コンフィグの表示
- /piechart saveConfig : コンフィグを保存
- /piechart reloadConfig : コンフィグをリロード



![2023-01-03_07 26 26](https://user-images.githubusercontent.com/115648249/210281907-26654a0a-967b-422e-aac9-6fee356c9c98.png)

![アートボード 1](https://user-images.githubusercontent.com/115648249/229098893-b67d3c7c-5101-4c69-8da1-242109e40989.png)

