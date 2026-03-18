#  ỨNG DỤNG HỌC TẬP TRẮC NGHIỆM CÓ PHÂN QUYỀN

*(Java + MySQL – Kiến trúc MVC)*

---

## Video demo

Link Drive: https://drive.google.com/drive/folders/1PwhJSnk0-OOAkqZtnZ4ZPp4JSW-d-v3W?usp=sharing

## 1. Giới thiệu đề tài

Ứng dụng học tập trắc nghiệm có phân quyền là hệ thống hỗ trợ tạo, quản lý và làm bài thi trắc nghiệm trên máy tính.

Hệ thống được xây dựng nhằm:

* Hỗ trợ học tập và ôn luyện kiến thức
* Quản lý ngân hàng câu hỏi
* Tổ chức thi trắc nghiệm tự động
* Thống kê và đánh giá kết quả học tập

### Công nghệ sử dụng:

* Ngôn ngữ lập trình: **Java**
* Cơ sở dữ liệu: **MySQL**
* Kiến trúc phần mềm: **MVC**
* Kết nối CSDL: **JDBC**
* IDE phát triển: **Apache NetBeans**

---

## 2. Phân quyền hệ thống

### ADMIN

* Quản lý người dùng
* Quản lý bộ câu hỏi
* Quản lý danh mục (cấp học – môn học)
* Thống kê và báo cáo hệ thống
* Đặt lại mật khẩu của mình
### NGƯỜI DÙNG

* Làm bài trắc nghiệm
* Tải lên bộ câu hỏi
* Quản lý bộ câu hỏi cá nhân
* Xem kết quả học tập
* Đặt lại mật khẩu của mình
---

## 3. Chức năng chi tiết

---

### 3.1 Quản lý người dùng (Admin)

* Thêm / sửa / xóa người dùng
* Khóa / mở khóa tài khoản
* Phân quyền (ADMIN / USER)

---

### 3.2 Quản lý bộ câu hỏi (Admin)

* Duyệt bộ câu hỏi người dùng tải lên
* Chấp nhận / từ chối bộ câu hỏi
* Sửa / xóa câu hỏi
* Gán trạng thái:

  * `công khai`
  * `riêng tư`

---

### 3.3 Quản lý danh mục học tập

* Quản lý **cấp học**

  * Tiểu học
  * THCS
  * THPT
  * Đại học

* Quản lý **môn học**

  * Toán
  * Lý
  * Hóa
  * CNTT

---

### 3.4 Làm bài trắc nghiệm

* Chọn cấp học – môn học
* Chọn số câu hỏi
* Chọn thời gian làm bài
* Trộn câu hỏi và đáp án
* Đếm ngược thời gian
* Tự động nộp bài khi hết giờ

---

### 3.5 Chấm điểm tự động

* Tính tổng điểm
* Đếm số câu đúng
* Đếm số câu sai
* Hiển thị đáp án đúng
* Lưu lịch sử làm bài

---

### 3.6 Tải lên bộ câu hỏi

* Upload file:

  * `.xlsx`

* Hệ thống tự động đọc file
* Tạo câu hỏi và đáp án
* Gửi Admin duyệt

---

### 3.7 Thống kê và báo cáo

* Tổng số người dùng
* Tổng số bài thi
* Điểm trung bình theo môn học
* Top người dùng có điểm cao
* Theo dõi tiến bộ học tập theo thời gian

---

## 4. Chức năng nâng cao (Cộng điểm)

* Tạo đề thi thông minh (cân bằng mức độ: dễ – trung bình – khó)
* Chế độ luyện tập (hiển thị đáp án sau mỗi câu)
* Danh sách câu hỏi yêu thích
* Xuất file Excel
* Xuất file PDF
* In kết quả học tập

---

## 🗄️ 5. Thiết kế cơ sở dữ liệu (MySQL)

### Các bảng chính:

* `vai_tro`
* `nguoi_dung`
* `cap_hoc`
* `mon_hoc`
* `bo_cau_hoi`
* `cau_hoi`
* `dap_an`
* `bai_thi`
* `chi_tiet_bai_thi`
* `cau_hoi_yeu_thich`

---

## 6. Kiến trúc hệ thống

Ứng dụng được xây dựng theo mô hình **MVC**:

* **Model**: Entity, DAO, Service xử lý dữ liệu
* **View**: Giao diện Java Swing / JavaFX
* **Controller**: Điều khiển luồng xử lý giữa View và Model

---

## 7. Mục tiêu đạt được

* Xây dựng hệ thống có phân quyền rõ ràng
* Thực hiện chấm điểm tự động chính xác
* Áp dụng kết nối CSDL bằng JDBC
* Thiết kế CSDL chuẩn hóa
* Có chức năng nâng cao phục vụ thực tế giáo dục
