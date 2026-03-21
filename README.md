# ỨNG DỤNG HỌC TẬP TRẮC NGHIỆM CÓ PHÂN QUYỀN
*(Java Swing + MySQL – Kiến trúc MVC, phát triển bằng Apache NetBeans/Ant)*

---

## Video demo

Link Drive: https://drive.google.com/file/d/1SBuAcFTgob5_sQjWZ3oi_UOFFjak2rGL/view?usp=sharing

## Video hướng dẫn cài đặt trên hệ điều hành MacOs

Link Drive: https://drive.google.com/file/d/11q25Lmmt6FXh8BX0aY_SJb4XOmFyLPa9/view?usp=sharing

---

## 1. Giới thiệu

Ứng dụng hỗ trợ **tạo – quản lý – làm bài thi trắc nghiệm** trên máy tính, có **phân quyền ADMIN/USER** và lưu dữ liệu trên **MySQL**.

Điểm nổi bật:

* Đăng nhập/đăng ký, khoá tài khoản, quên mật khẩu bằng **OTP email**
* Quản lý danh mục **Cấp học** và **Môn học**
* Quản lý **Bộ câu hỏi / Câu hỏi / Đáp án** + duyệt bộ câu hỏi
* Làm bài thi, chấm điểm tự động, lưu **lịch sử thi**
* **Yêu thích câu hỏi**, xem bảng xếp hạng
* Thống kê/báo cáo và **xuất báo cáo Excel**
* Tính năng AI (Gemini): **tạo bộ câu hỏi** và **giải thích đáp án**

---

## 2. Công nghệ & thư viện

* **Java** (UI: **Swing** + NetBeans `.form`)
* **MySQL** + **JDBC**
* Thư viện sử dụng (đã có trong `UngDungTracNghiem/dist/lib`):
  * MySQL Connector/J
  * Apache POI (Excel + Word `.docx`)
  * OpenCSV
  * Gson
  * Jakarta Mail (gửi OTP)
  * Log4j

---

## 3. Phân quyền hệ thống

Trong bảng `nguoi_dung`:

* `vai_tro_id = 1` → **ADMIN**
* `vai_tro_id != 1` → **USER**
* `trang_thai = 0` → tài khoản **bị khoá** (không đăng nhập được)

### ADMIN

* Quản lý người dùng: thêm/sửa/xoá, khoá/mở khoá, phân quyền
* Quản lý bộ câu hỏi: duyệt/trạng thái, công khai/riêng tư, chỉnh sửa nội dung
* Quản lý danh mục: cấp học – môn học
* Thống kê & báo cáo + xuất Excel

### USER

* Làm bài trắc nghiệm, xem chi tiết bài thi
* Xem lịch sử thi, bảng xếp hạng
* Đánh dấu **câu hỏi yêu thích**
* Tải lên / tạo bộ câu hỏi (gửi ADMIN duyệt)
* Cập nhật thông tin cá nhân

---

## 4. Tính năng AI (Gemini)

Ứng dụng có 2 luồng AI chính:

* **Tạo bộ câu hỏi bằng AI** (màn hình tải lên/tạo bộ câu hỏi): AI trả về JSON câu hỏi + đáp án + đáp án đúng và hệ thống lưu vào CSDL.
* **Giải thích đáp án** khi xem chi tiết bài thi: AI giải thích vì sao đáp án đúng, lỗi sai của thí sinh, kiến thức cần nhớ.

Cấu hình API key xem mục “Cấu hình”.

---

## 5. Tải lên bộ câu hỏi (file) – định dạng hỗ trợ

Màn hình “Tải lên / Tạo bộ câu hỏi” hỗ trợ chọn:

* **Excel**: `.xlsx`
* **CSV**: `.csv`
* **Word**: `.docx`

### 5.1 Định dạng Excel/CSV (khuyến nghị)

Mỗi dòng là 1 câu hỏi với 6 cột theo thứ tự:

1. `question` (hoặc “Câu hỏi”): nội dung câu hỏi
2. `A`: đáp án A
3. `B`: đáp án B
4. `C`: đáp án C
5. `D`: đáp án D
6. `correct`: đáp án đúng (**A/B/C/D** hoặc **1/2/3/4**)

Hàng tiêu đề (nếu có) sẽ được tự bỏ qua.

### 5.2 Định dạng Word (.docx)

* Mỗi câu bắt đầu bằng chuỗi **`Câu hỏi:`**
* Đáp án đặt cùng đoạn hoặc các đoạn sau, có nhãn **A/B/C/D** (ví dụ: `A. ...`, `B) ...`)
* **Đáp án đúng được bôi đậm (Bold)** trong file Word

---

## 6. Cơ sở dữ liệu (MySQL)

Ứng dụng kết nối MySQL tại:

* File: `UngDungTracNghiem/src/database/KetNoiDB.java`
* URL đang cấu hình trong code:
  `jdbc:mysql://mysql-22383302-lenguyenthaibao.g.aivencloud.com:19341/tracnghiem?sslMode=REQUIRED&serverTimezone=Asia/Ho_Chi_Minh`

Bạn cần tạo database và các bảng tương ứng (tham khảo truy vấn trong `UngDungTracNghiem/src/dao`).
Các bảng đang được sử dụng trong code:

* `nguoi_dung`
* `cap_hoc`
* `mon_hoc`
* `bo_cau_hoi`
* `cau_hoi`
* `dap_an`
* `bai_thi`
* `chi_tiet_bai_thi`
* `cau_hoi_yeu_thich` (có hàm tạo nếu chưa có)

---

## 7. Chạy ứng dụng

### Cách 1: Chạy từ NetBeans (khuyến nghị khi phát triển)

1. Mở project `UngDungTracNghiem` bằng Apache NetBeans
2. Cài đúng JDK (project đang cấu hình `javac.source=25`, `javac.target=25`)
3. Run project (main class mặc định: `view.FrmDangNhap`)

### Cách 2: Chạy từ file JAR đã build

Từ thư mục `UngDungTracNghiem/dist`:

```bash
java -jar UngDungTracNghiem.jar
```

---

## 8. Cấu hình (không hardcode)

### 8.1 Gemini API Key

Đặt một trong hai cách:

* Biến môi trường: `GEMINI_API_KEY`
* JVM args: `-Dgemini.api.key=YOUR_KEY`

### 8.2 Cấu hình DB (bắt buộc)

Đặt biến môi trường trước khi chạy:

* `DB_USER`
* `DB_PASS`

Ví dụ:

```bash
export DB_USER=your_db_user
export DB_PASS=your_db_password
```

### 8.3 SMTP gửi OTP (Quên mật khẩu)

Đặt một trong hai cách:

* Biến môi trường: `APP_OTP_EMAIL`, `APP_OTP_PASSWORD`
* JVM args: `-Dapp.otp.email=... -Dapp.otp.password=...`

Gợi ý: với Gmail nên dùng **App Password 16 ký tự**.

---

## 9. Kiến trúc thư mục (MVC)

* `UngDungTracNghiem/src/model`: model/entity
* `UngDungTracNghiem/src/dao`: truy xuất dữ liệu (JDBC)
* `UngDungTracNghiem/src/view`: giao diện Swing (NetBeans `.form`)
* `UngDungTracNghiem/src/controller`: entrypoint/điều phối
* `UngDungTracNghiem/src/utils`: tiện ích (UI, AI service, quản lý phiên)
* `UngDungTracNghiem/src/database`: kết nối CSDL.
