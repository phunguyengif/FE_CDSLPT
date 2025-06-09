# FE_CDSLPT
# Mô hình dữ liệu phân tán trong Quản lý thông tin học tập

## 1. Giới thiệu
Đề tài nghiên cứu và triển khai mô hình cơ sở dữ liệu phân tán phục vụ cho việc quản lý thông tin học tập. Hệ thống được xây dựng nhằm đảm bảo tính sẵn sàng, phân mảnh dữ liệu theo khu vực hoặc bộ môn, và hỗ trợ mở rộng.

## 2. Mục tiêu và các tính năng
- Mục tiêu chính của hệ thống là quản lý hiệu quả các hoạt động của hệ thống trường học có nhiều nhánh, bao gồm quản lý thông tin học sinh, giáo viên, khóa học và điểm số của từng học sinh. 
- Cơ sở dữ liệu phân tán đề xuất: Phân tán dựa theo BranchID(Chi nhánh). Hệ thống sẽ bao gồm nhiều thành phần con. Đầu tiên là hệ thống quản lý thông tin học sinh, cho phép lưu trữ và quản lý hồ sơ học sinh, bao gồm thông tin cá nhân, điểm số và lớp học. Tiếp theo là hệ thống quản lý khóa học, cung cấp danh sách các khóa học, môn học và lớp học tương ứng với từng môn học. Cuối cùng, hệ thống quản lý điểm số sẽ theo dõi và quản lý điểm số của từng học sinh đối với các môn học mà họ đã đăng ký.
- Cài đặt và triển khai hệ thống với 3 node tương ứng với chi nhánh trung tâm và 2 chi nhánh bắc và nam. Hỗ trợ truy vấn nhanh tại các chi nhánh

## 3. Kiến trúc hệ thống
- Frontend: NetBean kết hợp với giao diện kéo thả JavaSwing
- Backend: Java
- Database: Sql Server
- Cấu trúc dự án 
src/main
  java/com/mycompany
    DAO
      DangKyDAO.java
      DiemDAO.java
      LopHocDAO.java
      MonHocDAO.java
      SinhVienDAO.java
      UserDAO.java
    DatabaseHelper
      DatabaseConnectionManager.java
    model
      DangKy.java
      Diem.java
      LopHoc.java
      MonHoc.java
      SinhVien.java
    qlsv
      HomeForm.form
      HomeForm.java
      LoginForm.form
      LoginForm.java
      QLSV.java
resources/com.company.icon
target
README.md
pom.xml

## 4. Hướng dẫn cài đặt
1. Clone dự án https://github.com/phunguyengif/FE_CDSLPT.git
   Clone database 
2. Tải NetBean https://netbeans.apache.org/front/main/download/nb22/ tải file Apache-NetBeans-22-bin-windows-x64.exe
3. Tải các phụ thuộc:
    Jdk 22: 	https://download.oracle.com/java/22/archive/jdk-22.0.2_windows-x64_bin.exe (sha256)
4. Chạy ứng dụng:
    Trong NetBean mở project FE_CSDLPT vừa mới pull từ git về
    Trong Project Files -> pom.xml thêm file dependence để thêm vào phương thức kết nối với database
        <dependencies>
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <version>10.2.3.jre17</version>
        </dependency>
    </dependencies>
    Run file QLSV để chạy dự án
## 4.Hướng dẫn sử dụng
1. Vai trò giáo viên 
   Chọn chi nhánh
   Đăng nhập vào hệ thống bằng tài khoản giáo viên (Tk: admin_hcm, mk: 123456)
   Sau khi đăng nhập, sẽ chuyển sang giao diện chính: gồm các phần như quản lý sinh viên, quản lý môn học, quản lý điểm, quản lý lớp học
   Thực hiện các chức năng quản lý
2. Vai trò sinh viên
   Chọn chi nhánh 
   Tick vào ô Sinh Viên
   Đăng nhập vào hệ thống bằng tài khoản Sinh viên (TK: Mã số sinh viên, MK: Tên sinh viên)
   Thực hiện chức năng đăng ký lớp học
3. Ví dụ 1 vài chức năng
   *Thêm sinh viên:
      Sau khi giáo viên đăng nhập thành công
      Giao diện sinh viên bao gồm các chức năng thêm, xóa, sửa sinh viên
      Tại giao diện quản lý sinh viên: Giáo viên nhấn vào button thêm rồi điền đầy đủ thông tin sinh viên sau đó nhấn vào button lưu.
      Nếu lưu thành công thì thông báo "Thêm sinh viên thành công". Ngược lại thông báo lỗi.
### Yêu cầu hệ thống
- SQL Server, Java, NetBean, jdk 22
## 5.Tác giả và bản quyền
1. Tác giả : Nguyễn Thanh Phú, N22DCCN060


