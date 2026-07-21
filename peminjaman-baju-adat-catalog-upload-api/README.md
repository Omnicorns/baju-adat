# Peminjaman Baju Adat + Catalog API

Project Spring Boot sederhana untuk katalog dan peminjaman baju adat.

## Menjalankan project

```bash
mvn spring-boot:run
```

Buka API:

```text
http://localhost:8080
```

Database bawaan menggunakan H2.

## Catalog API

### Ambil semua katalog aktif

```http
GET /api/catalog
```

Filter yang tersedia:

```http
GET /api/catalog?keyword=jawa
GET /api/catalog?daerah=Jawa
GET /api/catalog?tersedia=true
GET /api/catalog?keyword=jawa&tersedia=true
```

### Detail katalog

```http
GET /api/catalog/{id}
```

### Tambah katalog

```http
POST /api/catalog
Content-Type: application/json
```

```json
{
  "kode": "SND-001",
  "nama": "Pakaian Adat Sunda",
  "daerah": "Sunda",
  "deskripsi": "Set pakaian adat Sunda.",
  "ukuran": "M, L, XL",
  "harga": 160000,
  "stok": 3,
  "imageUrl": "/catalog/beskap-jawa.svg",
  "aktif": true
}
```

### Ubah katalog

```http
PUT /api/catalog/{id}
```

Body sama seperti endpoint tambah katalog.

### Ubah stok

```http
PATCH /api/catalog/{id}/stok
Content-Type: application/json
```

```json
{
  "stok": 5
}
```

### Hapus katalog

```http
DELETE /api/catalog/{id}
```

## Peminjaman menggunakan katalog

```http
POST /api/peminjaman
Content-Type: application/json
```

```json
{
  "namaDepan": "Aloysius",
  "namaBelakang": "Wibowo",
  "alamat": "Jakarta Pusat",
  "nomorWhatsapp": "081234567890",
  "catalogId": 1,
  "catatan": "Ukuran L",
  "startDate": "2026-07-25T10:30:00",
  "endDate": "2026-07-27T17:00:00"
}
```

Saat `catalogId` dikirim, backend otomatis mengambil kode, nama, dan harga baju dari katalog.

Format lama tetap didukung:

```json
{
  "namaDepan": "Aloysius",
  "namaBelakang": "Wibowo",
  "alamat": "Jakarta Pusat",
  "nomorWhatsapp": "081234567890",
  "jenisBaju": "Jawa",
  "catatan": "Ukuran L",
  "startDate": "2026-07-25T10:30:00",
  "endDate": "2026-07-27T17:00:00"
}
```

## Data awal

Saat database masih kosong, aplikasi otomatis membuat lima data katalog:

- Beskap Jawa Cokelat
- Kebaya Jawa Merah
- Pakaian Adat Bali
- Pakaian Batak Ulos
- Pakaian Adat Aceh

## PostgreSQL

Buat database:

```sql
CREATE DATABASE db_peminjaman_baju;
```

Jalankan:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgresql
```

Ubah konfigurasi pada:

```text
src/main/resources/application-postgresql.properties
```

## Upload gambar katalog

Upload menggunakan `multipart/form-data` setelah data katalog dibuat.

```http
POST /api/catalog/{id}/image
Content-Type: multipart/form-data
```

Nama field file harus:

```text
file
```

Contoh cURL:

```bash
curl -X POST "http://localhost:8080/api/catalog/1/image" \
  -F "file=@C:/Users/User/Pictures/beskap.jpg"
```

Format yang diterima:

```text
JPG, PNG, WEBP
```

Ukuran maksimal:

```text
5 MB
```

Response akan mengembalikan `imageUrl`, contohnya:

```json
{
  "id": 1,
  "nama": "Beskap Jawa Cokelat",
  "imageUrl": "/uploads/catalog/f4ab7f1a-97e7-4ac7-a8fa-15c7485e7bd1.jpg"
}
```

Gambar dapat dibuka melalui:

```text
http://localhost:8080/uploads/catalog/f4ab7f1a-97e7-4ac7-a8fa-15c7485e7bd1.jpg
```

Hapus gambar katalog:

```http
DELETE /api/catalog/{id}/image
```

File disimpan di folder eksternal berikut supaya tidak hilang ketika project di-build ulang:

```text
uploads/catalog
```
