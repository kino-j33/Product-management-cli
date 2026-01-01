# CLI商品管理システム

Java と PostgreSQL を用いて作成した **CLI（コマンドライン）商品管理システム** です。  
Maven による依存関係管理と、設定ファイルの外部化を行い、  
**セキュリティと保守性を意識した構成** としています。

---

## 概要

本アプリケーションは、商品情報をデータベースで管理し、  
CLI 上から **検索・登録・変更・削除（論理削除）** を行うことができるシステムです。

検索機能では、  
**何も入力せずに Enter を押すことで全商品を一覧表示** できます。

学習およびポートフォリオ用途として、以下の点を重視して設計・実装しています。

- Maven 標準ディレクトリ構成の理解と実践
- DB接続情報の外部化および Git 管理対象外化
- 責務分離を意識したクラス設計
- JDBC を用いたデータアクセス処理

---

## 使用技術

- Java 21  
- Maven  
- PostgreSQL  
- JDBC  

---

## ディレクトリ構成

```
src
└─ main
  ├─ java
  │ ├─ data // DB接続・データ取得
  │ ├─ service // 業務ロジック
  │ ├─ selector // メニュー制御
  │ └─ Main.java // エントリーポイント
  └─ resources
     └─ db.properties

```

※ `src/main/resources/db.properties` は **`.gitignore` により Git 管理対象外** です。

---

## 機能一覧

### 商品管理機能

- 商品キーワード検索（ID / コード / 商品名）  
  未入力で Enter を押すと全商品を表示
- 商品登録  
- 商品情報変更  
- 商品削除（論理削除）

---

## DB接続設定について（重要）

DB接続情報は `src/main/resources/db.properties` に記述します。  
**本ファイルは `.gitignore` によりリポジトリには含まれません。**

### 設定ファイル例（ローカル用）

```
db.url=jdbc:postgresql://localhost:5432/your_database
db.user=postgres
db.password=password
db.driver=org.postgresql.Driver