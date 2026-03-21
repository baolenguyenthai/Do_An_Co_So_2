# HỆ THỐNG QUẢN LÝ VÀ THI TRẮC NGHIỆM THÔNG MINH (Java Swing + MySQL)

Ứng dụng desktop hỗ trợ tạo bộ câu hỏi, thi trắc nghiệm, chấm điểm tự động, thống kê và báo cáo, có phân quyền `ADMIN`/`USER`.

## Video demo:

- Link video demo: <https://drive.google.com/file/d/1SBuAcFTgob5_sQjWZ3oi_UOFFjak2rGL/view?usp=sharing>

## Video hướng dẫn cài đặt trên hệ điều hành WINDOWS

- Link video cài đặt <https://drive.google.com/file/d/1ggcxpaPIBe3__5Al2lesEIQDTDDQd8d9/view?usp=sharing>

## Video hướng dẫn cài đặt trên hệ điều hành MACOS:

- Link video cài đặt macOS: <https://drive.google.com/file/d/11q25Lmmt6FXh8BX0aY_SJb4XOmFyLPa9/view?usp=sharing>

## Tính năng chính

- Đăng nhập, đăng ký, quên mật khẩu qua OTP email.
- Phân quyền:
1. `ADMIN`: quản lý người dùng, môn học, bộ câu hỏi, duyệt bộ câu hỏi, thống kê/báo cáo.
2. `USER`: làm bài thi, xem lịch sử kết quả, bảng xếp hạng, câu hỏi yêu thích, tải lên/tạo bộ câu hỏi.
- Tạo bộ câu hỏi bằng AI (Gemini) và tải lên từ file (luồng lưu CSDL hiện đã hoàn thiện cho `.docx`).
- Xem chi tiết bài thi và gọi AI giải thích đáp án.
- Xuất báo cáo chi tiết ra Excel.

## Công nghệ sử dụng

- Java Swing (NetBeans Form).
- JDBC + MySQL.
- Gson.
- OpenCSV.
- Apache POI (Excel/Word).
- Jakarta Mail (SMTP OTP).

Thư viện runtime nằm trong `UngDungTracNghiem/dist/lib`.

## Cấu trúc dự án

```text
Do_An_Co_So_2/
├── README.md
├── database/
│   └── database.sql
├── UngDungTracNghiem/
│   ├── src/
│   │   ├── controller/
│   │   ├── dao/
│   │   ├── database/
│   │   ├── model/
│   │   ├── utils/
│   │   └── view/
│   ├── dist/
│   │   ├── UngDungTracNghiem.jar
│   │   └── lib/
│   └── nbproject/
├── UngDungTracNghiem-Setup.exe
└── UngDungTracNghiem-1.0.dmg
```

## Yêu cầu môi trường

- JDK 25 (`javac.source=25`, `javac.target=25`).
- MySQL 8+.
- (Tùy chọn) Apache NetBeans nếu phát triển từ source.
- (Tùy chọn) Apache Ant nếu build qua dòng lệnh.

## Thiết lập cơ sở dữ liệu

1. Tạo database `tracnghiem`.
2. Import schema + dữ liệu mẫu:

```bash
mysql -u <user> -p tracnghiem < database/database.sql
```

3. Kiểm tra kết nối tại file:

- `UngDungTracNghiem/src/database/KetNoiDB.java`

Mặc định code đang dùng:

- `DB_URL`: `jdbc:mysql://mysql-22383302-lenguyenthaibao.g.aivencloud.com:19341/tracnghiem?sslMode=REQUIRED&serverTimezone=Asia/Ho_Chi_Minh`
- `HARDCODED_DB_USER`
- `HARDCODED_DB_PASS`

Bạn cần cập nhật thông tin DB đúng môi trường của bạn trước khi chạy.

## Cấu hình AI Gemini

`GeminiService` hỗ trợ đọc API key theo thứ tự:

1. Biến môi trường `GEMINI_API_KEY`
2. JVM property `-Dgemini.api.key=...`
3. `HARDCODED_API_KEY` trong `UngDungTracNghiem/src/utils/GeminiService.java`

Tùy chọn model:

- `GEMINI_MODEL` hoặc `-Dgemini.model=...`

## Cấu hình SMTP OTP (Quên mật khẩu)

`NguoiDungDAO.guiOTP(...)` đọc cấu hình theo thứ tự:

1. `APP_OTP_EMAIL`, `APP_OTP_PASSWORD`
2. `-Dapp.otp.email=... -Dapp.otp.password=...`
3. Giá trị fallback trong code

Khuyến nghị dùng Gmail App Password 16 ký tự.

## Chạy ứng dụng

### Cách 1: Chạy bản đóng gói (khuyến nghị nhanh)

```bash
cd UngDungTracNghiem/dist
java -jar UngDungTracNghiem.jar
```

Hoặc dùng file cài đặt:

- Windows: `UngDungTracNghiem-Setup.exe`
- macOS: `UngDungTracNghiem-1.0.dmg`

### Cách 2: Chạy từ NetBeans (phát triển)

1. Mở project `UngDungTracNghiem`.
2. Đảm bảo JDK 25.
3. Main class: `view.FrmDangNhap`.
4. Nếu thiếu thư viện do đường dẫn local cũ trong `nbproject/project.properties`, re-link lại từ `UngDungTracNghiem/dist/lib`.

## Build từ source

Project theo chuẩn NetBeans/Ant (`build.xml`).

```bash
cd UngDungTracNghiem
ant clean jar
```

Lưu ý: máy cần cài sẵn `ant`.

## Định dạng file tải lên bộ câu hỏi

Màn hình `TẢI LÊN / TẠO BỘ CÂU HỎI` hỗ trợ:

- `.xlsx`
- `.csv`
- `.docx`

Lưu ý hiện trạng:

- `.xlsx` và `.csv`: đang dùng tốt cho luồng đọc/preview nội dung.
- `.docx`: đã có luồng parse và lưu câu hỏi + đáp án vào CSDL.

### Excel/CSV

Mỗi dòng gồm 6 cột:

1. Nội dung câu hỏi
2. Đáp án A
3. Đáp án B
4. Đáp án C
5. Đáp án D
6. Đáp án đúng (`A/B/C/D` hoặc `1/2/3/4`)

### Word (.docx)

- Mỗi câu có tiền tố `Câu hỏi:`.
- Đáp án có nhãn `A/B/C/D` (ví dụ `A.`, `B)`, ...).
- Đáp án đúng được in đậm (Bold).

## Luồng phân quyền và nghiệp vụ quan trọng

- `vai_tro_id = 1`: vào `AdminForm`.
- `vai_tro_id != 1`: vào `UserForm`.
- `trang_thai = 0`: tài khoản bị khóa, không đăng nhập.
- Chỉ bộ câu hỏi `DA_DUYET` mới xuất hiện ở màn thi.
- Bộ câu hỏi không công khai (`cong_khai = 0`) sẽ:
1. Không cho xem chi tiết đáp án từ lịch sử.
2. Không cho làm lại nếu user đã thi bộ đó.
3. Không cho thêm câu hỏi vào danh sách yêu thích.

## Tài khoản mẫu (sau khi import `database.sql`)

- Admin: `admin / 12345678`
- User: `user1 / 123456`

## Lưu ý triển khai thực tế

- Mật khẩu người dùng hiện đang lưu plain text trong DB.
- Một số thông tin nhạy cảm hiện có fallback trong code.
- Nên chuyển toàn bộ secrets sang biến môi trường/secret manager trước khi đưa production.

## Tác giả

- Lê Nguyễn Thái Bảo
