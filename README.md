# MCServerStatus

## 概要
ServerListPing( https://wiki.vg/Server_List_Ping )をJavaで実装し、WebAPIとして利用できるようにしたもの。

ServerListPing実装部分はこれ  
https://github.com/ttk1/ServerListPing  

## API仕様

| Method | URI | Description |
| ---- | ---- | ---- |
| GET | /server/{host} | hostを指定しての情報取得(ポートは25565固定) |
| GET | /server/{host}/{port} | hostとportを指定して情報取得 |
| GET | /version | APIのバージョン表示 |

### 例
ホスト: `minecraft.example.com` , ポート: `12345` を指定して情報を取得する。

```
GET /server/minecraft.example.com/12345
```

## 注意
hostとしてIPアドレスを指定することはできません。  
自分用で使う程度にしか作りこんでません。使う際は自己責任で。
