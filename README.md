# Book Management System API

Kotlin, Spring Boot, jOOQ, Flyway を使用した書籍管理システムのバックエンドAPIです。

## 技術スタック

- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.2.11
- **Database Access**: jOOQ 3.18.11
- **Migration**: Flyway
- **DB**: PostgreSQL (Docker Compose)
- **Testing**: JUnit 5, MockK

## 機能・制約

### 書籍 (Book)

- タイトル、価格、複数著者、出版状況を管理。
- 価格は0以上であること。
- 最低1人の著者を持つこと。
- 「出版済み」から「未出版」へのステータス変更不可。

### 著者 (Author)

- 名前、生年月日を管理。
- 生年月日は現在日以前であること。
- 1人の著者が複数の書籍を執筆可能。

## 実行方法

### 1. データベースの起動

Docker Compose を使用して PostgreSQL を起動します。

```bash
docker compose up -d
```

### 2. アプリケーションの起動

デフォルトのポートは **8081** に設定されています（8080の競合を避けるため）。

```bash
$env:GRADLE_USER_HOME="C:\gradle_home" # Windows環境の場合
./gradlew bootRun
```

### 3. テストの実行

```bash
./gradlew test
```

## API エンドポイント

- `GET /api/books`: 全書籍の取得
- `GET /api/authors`: 全著者の取得
- `POST /api/books`: 書籍の登録
- `POST /api/authors`: 著者の登録
- `GET /api/authors/{id}/books`: 特定の著者に紐づく書籍の取得

詳細は `test-api.http` ファイルで確認・実行できます。
