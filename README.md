# PieChartPlugin
スキンの色割合を円グラフで表示するプラグイン
## コマンド
- /piechart get MCID
### 注意
地図にすると配色が変わるため微妙

### 内部処理
1. JSONからスキン画像を取得
1. 円グラフを作成
1. 地図を作成

### 種類
- original : 色を降順に並び変えたもの
- clustered : 類似色を纏めたグラフ(originalと色の比率が変わる)
- sorted : 類似色が並ぶように、originalを並び替えたグラフ

![2023-01-03_07 26 26](https://user-images.githubusercontent.com/115648249/210281907-26654a0a-967b-422e-aac9-6fee356c9c98.png)
