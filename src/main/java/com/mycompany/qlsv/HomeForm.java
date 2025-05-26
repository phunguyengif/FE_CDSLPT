/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.qlsv;

import com.mycompany.DAO.DangKyDAO;
import com.mycompany.DAO.DiemDAO;
import com.mycompany.DAO.LopHocDAO;
import com.mycompany.DAO.MonHocDAO;
import com.mycompany.DAO.SinhVienDAO;
import com.mycompany.model.DangKy;
import com.mycompany.model.Diem;
import com.mycompany.model.LopHoc;
import com.mycompany.model.MonHoc;
import com.mycompany.model.SinhVien;
import java.util.*;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ntphu
 */
public class HomeForm extends javax.swing.JFrame {

    private DefaultTableModel tblModel = new DefaultTableModel();
    private DefaultTableModel tblModelMH = new DefaultTableModel();
    private DefaultTableModel tblModelDiem = new DefaultTableModel();
    private DefaultTableModel tblModelLopHoc = new DefaultTableModel();
    private DefaultComboBoxModel cbModelLop = new DefaultComboBoxModel();
    private DefaultComboBoxModel cbModelMH = new DefaultComboBoxModel();

    private DefaultTableModel tblModelDK = new DefaultTableModel();
    private String branchID;

    /**
     * Creates new form HomeForm
     */
    public HomeForm(String branchID, boolean isStudent, String username, String password) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.branchID = branchID;
        txtChiNhanh.setText(branchID);
        txtDKMaSV.setText(username);
        txtDKSV.setText(password);

        if (isStudent) {
            // Giữ lại tab Đăng ký (jPanel5) thôi, xóa các tab còn lại
            int tabCount = jTabbedPane1.getTabCount();
            initTableDK();
            fillTableDK(username);
            initComboBoxDK();

            for (int i = tabCount - 1; i >= 0; i--) {
                java.awt.Component tabComponent = jTabbedPane1.getComponentAt(i);

                // Giả sử jPanel5 là biến JPanel đăng ký
                if (tabComponent != jPanel5) {
                    jTabbedPane1.remove(i);
                }
            }
        } else {
            // Chạy logic thông thường cho các tài khoản không phải sinh viên
            initTable();
            fillTable();

            initTableMH();
            fillTableMH();

            initComboBoxLop();
            initTableDiem();
            initComboBoxLopListener();

            initComboBoxMH();
            initTableLopHoc();
            fillTableLopHoc();
        }
    }

    private void initComboBoxLopListener() {
        cbxLopHoc.addActionListener(e -> {
            String selectedClassID = (String) cbxLopHoc.getSelectedItem();
            if (selectedClassID != null) {
                updateTableWithClassID(selectedClassID);
            }
        });
    }

    private void initComboBoxLop() {
        try {
            // Lấy danh sách tất cả lớp học từ cơ sở dữ liệu
            LopHocDAO dao = new LopHocDAO(branchID);
            List<LopHoc> lstLop = dao.getAllClasses();

            // Xóa các phần tử cũ trong model của ComboBox
            cbModelLop.removeAllElements();

            // Thêm từng ClassID vào model của ComboBox
            for (LopHoc lh : lstLop) {
                cbModelLop.addElement(lh.getClassID()); // Chỉ thêm ClassID
            }

            // Gán model đã cập nhật vào ComboBox
            cbxLopHoc.setModel(cbModelLop);
        } catch (Exception e) {
            // Hiển thị thông báo lỗi và in chi tiết lỗi ra console
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách lớp học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initComboBoxDK() {
        try {
            // Lấy danh sách tất cả lớp học từ cơ sở dữ liệu
            LopHocDAO dao = new LopHocDAO(branchID);
            List<LopHoc> lstLop = dao.getAllClasses();

            // Xóa các phần tử cũ trong model của ComboBox
            cbModelLop.removeAllElements();

            // Thêm từng ClassID vào model của ComboBox
            for (LopHoc lh : lstLop) {
                cbModelLop.addElement(lh.getClassID()); // Chỉ thêm ClassID
            }

            // Gán model đã cập nhật vào ComboBox
            cbxDKMaLop.setModel(cbModelLop);
        } catch (Exception e) {
            // Hiển thị thông báo lỗi và in chi tiết lỗi ra console
            JOptionPane.showMessageDialog(this, "Không thể tải danh sách lớp học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initComboBoxMH() {
        try {

            MonHocDAO dao = new MonHocDAO(branchID);
            List<MonHoc> lstmh = dao.getAllCourses();

            cbModelMH.removeAllElements();

            for (MonHoc mh : lstmh) {
                cbModelMH.addElement(new MonHoc(mh.getCourseID(), mh.getCourseName()));
            }

            // Gán model đã cập nhật vào ComboBox
            cbxMaMonHoc.setModel(cbModelMH);
        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Không thể tải danh sách môn học: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void resetFormSinhVien() {
        txtMaSV.setText("");
        txtTenSV.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtGioiTinh.setText("");
        txtBranchID.setText("");
    }

    private void resetFormMonHoc() {
        txtCourseID.setText("");
        txtCourseName.setText("");
        txtCredits.setText("");
    }

    private void resetFormDiem() {
        txtTenSVDiem.setText("");
        txtMaSVDiem.setText("");
        txtMidterm.setText("");
        txtFinal.setText("");
        txtAttendance.setText("");
    }

    private void resetFormLopHoc() {
        txtLHMaLop.setText("");
        txtLHTenLop.setText("");
        txtLHChiNhanh.setText("");
    }

    private void initTable() {
        String[] header = new String[]{"Mã SV", "Tên SV", "Ngày sinh", "Địa chỉ", "Giới tính", "Chi nhánh"};
        tblModel.setColumnIdentifiers(header);
        tblSinhVien.setModel(tblModel);
    }

    private void initTableMH() {
        String[] header = new String[]{"CourseID", "CourseName", "Credits"};
        tblModelMH.setColumnIdentifiers(header);
        tblMonHoc.setModel(tblModelMH);
    }

    private void initTableDiem() {
        String[] header = new String[]{"StudentID", "Full Name", "Midterm Score", "Final Score", "Attendance Score"};
        tblModelDiem.setColumnIdentifiers(header);
        tblDiem.setModel(tblModelDiem);
    }

    private void initTableLopHoc() {
        String[] header = new String[]{"ClassID", "ClassName", "BranchID", "BranchID"};
        tblModelLopHoc.setColumnIdentifiers(header);
        tblLopHoc.setModel(tblModelLopHoc);
    }

    private void initTableDK() {
        String[] header = new String[]{"StudentID", "ClassID"};
        tblModelDK.setColumnIdentifiers(header);
        tblDangKy.setModel(tblModelDK);
    }

    private void fillTable() {
        List<SinhVien> lstSinhVien = new ArrayList();
        try {
            SinhVienDAO dao = new SinhVienDAO(branchID);
            lstSinhVien = dao.getAllSinhVien();

        } catch (Exception e) {
            e.printStackTrace();
        }
        tblModel.setRowCount(0);
        for (SinhVien sv : lstSinhVien) {
            tblModel.addRow(new String[]{sv.getStudentID(), sv.getFullName(), sv.getBirthDate(), sv.getAddress(), sv.getGender(), sv.getBranchID()});
        }
        tblModel.fireTableDataChanged();
    }

    private void fillTableMH() {
        List<MonHoc> lstMonHoc = new ArrayList();
        try {
            MonHocDAO dao = new MonHocDAO(branchID);
            lstMonHoc = dao.getAllCourses();

        } catch (Exception e) {
            e.printStackTrace();
        }
        tblModelMH.setRowCount(0);
        for (MonHoc mh : lstMonHoc) {
            tblModelMH.addRow(new String[]{mh.getCourseID(), mh.getCourseName(), String.valueOf(mh.getCredit())});
        }
        tblModelMH.fireTableDataChanged();
    }

    private void updateTableWithClassID(String classID) {
        try {
            DiemDAO dao = new DiemDAO(branchID);
            List<Diem> lstDiem = dao.getByClassID(classID);

            // Xóa dữ liệu cũ trong bảng
            DefaultTableModel tableModel = (DefaultTableModel) tblDiem.getModel();
            tableModel.setRowCount(0);

            // Thêm dữ liệu mới vào bảng
            for (Diem diem : lstDiem) {
                tableModel.addRow(new Object[]{
                    diem.getStudentID(),
                    diem.getFullName(),
                    diem.getMidtermScore(),
                    diem.getFinalScore(),
                    diem.getAttendanceScore()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu điểm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void fillTableLopHoc() {
        List<LopHoc> lstLopHoc = new ArrayList();
        try {
            LopHocDAO dao = new LopHocDAO(branchID);
            lstLopHoc = dao.getAllClasses();

        } catch (Exception e) {
            e.printStackTrace();
        }
        tblModelLopHoc.setRowCount(0);
        for (LopHoc lh : lstLopHoc) {
            tblModelLopHoc.addRow(new String[]{lh.getClassID(), lh.getClassName(), lh.getCourseID(), lh.getBranchID()});
        }
        tblModelLopHoc.fireTableDataChanged();
    }

    private void fillTableDK(String studentID) {
        List<DangKy> lstDangKy = new ArrayList<>();
        try {
            DangKyDAO dao = new DangKyDAO(branchID);
            lstDangKy = dao.getDangKyByStudent(studentID);

        } catch (Exception e) {
            e.printStackTrace();
        }

        tblModelDK.setRowCount(0);
        for (DangKy dk : lstDangKy) {
            // Ví dụ hiển thị StudentID và ClassID trong bảng đăng ký
            tblModelDK.addRow(new String[]{dk.getStudentID(), dk.getClassID()});
        }
        tblModelDK.fireTableDataChanged();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        txtChiNhanh = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtMaSV = new javax.swing.JTextField();
        txtTenSV = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtGioiTinh = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtBranchID = new javax.swing.JTextField();
        txtNgaySinh = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSinhVien = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        btnSVThem = new javax.swing.JButton();
        btnSVXoa = new javax.swing.JButton();
        btnSVCapNhat = new javax.swing.JButton();
        btnSVLuu = new javax.swing.JButton();
        btnSVLoad = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtCourseID = new javax.swing.JTextField();
        txtCourseName = new javax.swing.JTextField();
        txtCredits = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMonHoc = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        btnMHThem = new javax.swing.JButton();
        btnMHXoa = new javax.swing.JButton();
        btnMHCapNhat = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnMHLoad = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cbxLopHoc = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDiem = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtTenSVDiem = new javax.swing.JTextField();
        txtMaSVDiem = new javax.swing.JTextField();
        txtMidterm = new javax.swing.JTextField();
        txtFinal = new javax.swing.JTextField();
        txtAttendance = new javax.swing.JTextField();
        jToolBar3 = new javax.swing.JToolBar();
        btnDiemLuu = new javax.swing.JButton();
        btnDiemCapNhat = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtDiemGradeID = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        btnLHThem = new javax.swing.JButton();
        btnLHXoa = new javax.swing.JButton();
        btnLHSua = new javax.swing.JButton();
        btnLHLuu = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtLHMaLop = new javax.swing.JTextField();
        txtLHTenLop = new javax.swing.JTextField();
        txtLHChiNhanh = new javax.swing.JTextField();
        cbxMaMonHoc = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblLopHoc = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDangKy = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtDKSV = new javax.swing.JTextField();
        txtDKMaSV = new javax.swing.JTextField();
        cbxDKMaLop = new javax.swing.JComboBox<>();
        jToolBar5 = new javax.swing.JToolBar();
        btnDKDangKy = new javax.swing.JButton();
        btnDKLoad = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        btnCauHinh = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel10.setText("Chi nhánh:");

        txtChiNhanh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChiNhanhActionPerformed(evt);
            }
        });

        jLabel4.setText("Mã SV:");

        jLabel5.setText("Tên SV:");

        jLabel6.setText("Ngày sinh:");

        jLabel7.setText("Địa chỉ:");

        jLabel8.setText("Giới tính:");

        txtMaSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSVActionPerformed(evt);
            }
        });

        jLabel9.setText("BrachID:");

        tblSinhVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSinhVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSinhVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSinhVien);

        jToolBar2.setRollover(true);

        btnSVThem.setText("Thêm  ");
        btnSVThem.setFocusable(false);
        btnSVThem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSVThem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSVThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVThemActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSVThem);

        btnSVXoa.setText("Xóa   ");
        btnSVXoa.setFocusable(false);
        btnSVXoa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSVXoa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSVXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVXoaActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSVXoa);

        btnSVCapNhat.setText("Cập nhật   ");
        btnSVCapNhat.setFocusable(false);
        btnSVCapNhat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSVCapNhat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSVCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVCapNhatActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSVCapNhat);

        btnSVLuu.setText("Lưu   ");
        btnSVLuu.setFocusable(false);
        btnSVLuu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSVLuu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSVLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVLuuActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSVLuu);

        btnSVLoad.setText("Load");
        btnSVLoad.setFocusable(false);
        btnSVLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSVLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSVLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSVLoadActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSVLoad);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDiaChi)
                    .addComponent(txtMaSV)
                    .addComponent(txtTenSV)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBranchID, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 869, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTenSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(txtGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(txtBranchID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SINH VIÊN", jPanel1);

        jLabel11.setText("Mã MH:");

        jLabel12.setText("Tên MH:");

        jLabel13.setText("Số tín chỉ:");

        txtCourseID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCourseIDActionPerformed(evt);
            }
        });

        tblMonHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblMonHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMonHocMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblMonHoc);

        jToolBar1.setRollover(true);

        btnMHThem.setText("Thêm    ");
        btnMHThem.setFocusable(false);
        btnMHThem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMHThem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMHThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMHThemActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMHThem);

        btnMHXoa.setText("Xóa   ");
        btnMHXoa.setFocusable(false);
        btnMHXoa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMHXoa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMHXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMHXoaActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMHXoa);

        btnMHCapNhat.setText("Cập nhật   ");
        btnMHCapNhat.setFocusable(false);
        btnMHCapNhat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMHCapNhat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMHCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMHCapNhatActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMHCapNhat);

        btnLuu.setText("Luu");
        btnLuu.setFocusable(false);
        btnLuu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLuu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLuu);

        btnMHLoad.setText("Load  ");
        btnMHLoad.setFocusable(false);
        btnMHLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMHLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnMHLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMHLoadActionPerformed(evt);
            }
        });
        jToolBar1.add(btnMHLoad);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCourseID, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(41, 41, 41)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtCredits, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 264, Short.MAX_VALUE))
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCourseID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtCredits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtCourseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("MÔN HỌC", jPanel2);

        cbxLopHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lớp Học" }));
        cbxLopHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxLopHocActionPerformed(evt);
            }
        });

        tblDiem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDiemMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblDiem);

        jLabel1.setText("Tên SV:");

        jLabel2.setText("Mã SV:");

        jLabel3.setText("MidtermScore:");

        jLabel15.setText("FinalScore:");

        jLabel16.setText("AttendanceScore:");

        txtMidterm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMidtermActionPerformed(evt);
            }
        });

        jToolBar3.setRollover(true);

        btnDiemLuu.setText("Lưu  ");
        btnDiemLuu.setFocusable(false);
        btnDiemLuu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDiemLuu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDiemLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiemLuuActionPerformed(evt);
            }
        });
        jToolBar3.add(btnDiemLuu);

        btnDiemCapNhat.setText("Cập nhật   ");
        btnDiemCapNhat.setFocusable(false);
        btnDiemCapNhat.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDiemCapNhat.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDiemCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiemCapNhatActionPerformed(evt);
            }
        });
        jToolBar3.add(btnDiemCapNhat);

        jLabel14.setText("GraesID:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMidterm, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenSVDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtMaSVDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cbxLopHoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiemGradeID, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(210, Short.MAX_VALUE))))
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cbxLopHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel14)
                    .addComponent(txtTenSVDiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaSVDiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiemGradeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(txtAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMidterm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("ĐIỂM", jPanel3);

        jToolBar4.setRollover(true);

        btnLHThem.setText("Thêm   ");
        btnLHThem.setFocusable(false);
        btnLHThem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLHThem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLHThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLHThemActionPerformed(evt);
            }
        });
        jToolBar4.add(btnLHThem);

        btnLHXoa.setText("Xóa   ");
        btnLHXoa.setFocusable(false);
        btnLHXoa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLHXoa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLHXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLHXoaActionPerformed(evt);
            }
        });
        jToolBar4.add(btnLHXoa);

        btnLHSua.setText("Sửa  ");
        btnLHSua.setFocusable(false);
        btnLHSua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLHSua.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLHSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLHSuaActionPerformed(evt);
            }
        });
        jToolBar4.add(btnLHSua);

        btnLHLuu.setText("Lưu   ");
        btnLHLuu.setFocusable(false);
        btnLHLuu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLHLuu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLHLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLHLuuActionPerformed(evt);
            }
        });
        jToolBar4.add(btnLHLuu);

        jLabel17.setText("Mã Lớp:");

        jLabel18.setText("Tên Lớp:");

        jLabel19.setText("Chi Nhánh:");

        cbxMaMonHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel20.setText("Mã môn học:");

        tblLopHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblLopHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLopHocMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblLopHoc);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtLHMaLop, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLHChiNhanh))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(txtLHTenLop, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 250, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxMaMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtLHMaLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxMaMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtLHTenLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtLHChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 23, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("LỚP HỌC", jPanel4);

        tblDangKy.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tblDangKy);

        jLabel21.setText("Sinh Viên:");

        jLabel22.setText("Mã Sinh Viên: ");

        cbxDKMaLop.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxDKMaLop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxDKMaLopActionPerformed(evt);
            }
        });

        jToolBar5.setRollover(true);

        btnDKDangKy.setText("Đăng ký   ");
        btnDKDangKy.setFocusable(false);
        btnDKDangKy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDKDangKy.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDKDangKy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDKDangKyActionPerformed(evt);
            }
        });
        jToolBar5.add(btnDKDangKy);

        btnDKLoad.setText("Load");
        btnDKLoad.setFocusable(false);
        btnDKLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDKLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDKLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDKLoadActionPerformed(evt);
            }
        });
        jToolBar5.add(btnDKLoad);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxDKMaLop, 0, 182, Short.MAX_VALUE)
                    .addComponent(txtDKSV))
                .addGap(80, 80, 80)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDKMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 301, Short.MAX_VALUE))
            .addComponent(jToolBar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtDKSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(txtDKMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(cbxDKMaLop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("ĐĂNG KÍ", jPanel5);

        jMenu1.setText("TRANG CHỦ");
        jMenuBar1.add(jMenu1);

        btnCauHinh.setText("CẤU HÌNH");
        btnCauHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCauHinhMouseClicked(evt);
            }
        });
        btnCauHinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCauHinhActionPerformed(evt);
            }
        });
        jMenuBar1.add(btnCauHinh);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txtChiNhanh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void txtMaSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaSVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaSVActionPerformed

    private void txtChiNhanhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChiNhanhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChiNhanhActionPerformed
//==================================Pages Mon Hoc =======================================
    private void btnMHThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMHThemActionPerformed
        // TODO add your handling code here:
        resetFormMonHoc();
        btnMHCapNhat.setEnabled(true);
    }//GEN-LAST:event_btnMHThemActionPerformed

    private void tblMonHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMonHocMouseClicked
        // TODO add your handling code here:
        int row = tblMonHoc.getSelectedRow();
        if (row >= 0) {
            txtCourseID.setText(tblMonHoc.getValueAt(row, 0).toString());
            txtCourseName.setText(tblMonHoc.getValueAt(row, 1).toString());
            txtCredits.setText(tblMonHoc.getValueAt(row, 2).toString());
            btnMHCapNhat.setEnabled(true);
            btnMHXoa.setEnabled(true);
        }
    }//GEN-LAST:event_tblMonHocMouseClicked

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        try {
            MonHoc mh = new MonHoc();
            mh.setCourseID(txtCourseID.getText());
            mh.setCourseName(txtCourseName.getText());
            mh.setCredits(Integer.parseInt(txtCredits.getText()));

            MonHocDAO dao = new MonHocDAO(branchID);
            dao.addCourse(mh);
            JOptionPane.showMessageDialog(this, "Thêm thành công Môn Học");
            fillTableMH();
            resetFormMonHoc();
            btnLuu.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnMHCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMHCapNhatActionPerformed
        // TODO add your handling code here:
        try {
            MonHoc mh = new MonHoc();
            mh.setCourseID(txtCourseID.getText());
            mh.setCourseName(txtCourseName.getText());
            mh.setCredits(Integer.parseInt(txtCredits.getText()));

            MonHocDAO dao = new MonHocDAO(branchID);
            dao.updateCourse(mh);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công Môn học");
            fillTableMH();
            resetFormMonHoc();
            btnMHCapNhat.setEnabled(false);
            btnMHXoa.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnMHCapNhatActionPerformed

    private void btnMHXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMHXoaActionPerformed
        // TODO add your handling code here:
        try {
            String courseId = txtCourseID.getText().trim();
            if (!courseId.isEmpty()) {
                MonHocDAO dao = new MonHocDAO(branchID);
                try {
                    if (dao.deleteCourse(courseId)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công môn học");
                        fillTableMH();
                        resetFormMonHoc();
                        btnMHCapNhat.setEnabled(false);
                        btnMHXoa.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể xóa môn học này");
                    }
                } catch (Exception ex) {
                    // Hiển thị thông báo lỗi chi tiết nếu môn học đã được liên kết với lớp học
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Môn học đã được đăng kí lớp", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã môn học để xóa.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMHXoaActionPerformed

    private void btnMHLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMHLoadActionPerformed
        // TODO add your handling code here:
        fillTableMH();
        resetFormMonHoc();
    }//GEN-LAST:event_btnMHLoadActionPerformed
//====================================Pages Sinh Vien===============================
    private void btnSVThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVThemActionPerformed
        // TODO add your handling code here:
        resetFormSinhVien();
        btnSVCapNhat.setEnabled(true);

    }//GEN-LAST:event_btnSVThemActionPerformed

    private void tblSinhVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSinhVienMouseClicked
        // TODO add your handling code here:
        int row = tblSinhVien.getSelectedRow();
        if (row >= 0) {
            txtMaSV.setText(tblSinhVien.getValueAt(row, 0).toString());
            txtTenSV.setText(tblSinhVien.getValueAt(row, 1).toString());
            txtNgaySinh.setText(tblSinhVien.getValueAt(row, 2).toString());
            txtDiaChi.setText(tblSinhVien.getValueAt(row, 3).toString());
            txtGioiTinh.setText(tblSinhVien.getValueAt(row, 4).toString());
            txtBranchID.setText(tblSinhVien.getValueAt(row, 5).toString());
        }
    }//GEN-LAST:event_tblSinhVienMouseClicked

    private void btnSVXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVXoaActionPerformed
        // TODO add your handling code here:
        try {
            if (!txtMaSV.getText().trim().isEmpty()) {
                SinhVienDAO dao = new SinhVienDAO(branchID);

                // Thử xóa sinh viên
                try {
                    if (dao.deleteSinhVien(txtMaSV.getText())) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công Sinh Viên");
                        fillTable(); // Cập nhật lại bảng sau khi xóa
                        resetFormSinhVien(); // Đặt lại các trường nhập liệu
                        btnMHCapNhat.setEnabled(false);
                        btnMHXoa.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể xóa sinh viên này. Sinh viên không tồn tại hoặc có lỗi khác.");
                    }
                } catch (Exception ex) {
                    // Xử lý lỗi khi sinh viên đã đăng ký lớp học
                    JOptionPane.showMessageDialog(this, "Không thể xóa sinh viên: " + ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sinh viên để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSVXoaActionPerformed

    private void btnSVCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVCapNhatActionPerformed
        // TODO add your handling code here:
        try {
            SinhVien sv = new SinhVien();
            sv.setStudentID(txtMaSV.getText());
            sv.setFullName(txtTenSV.getText());
            sv.setBirthDate(txtNgaySinh.getText());
            sv.setAddress(txtDiaChi.getText());
            sv.setGender(txtGioiTinh.getText());
            sv.setBranchID(txtBranchID.getText());

            SinhVienDAO dao = new SinhVienDAO(branchID);
            dao.updateSinhVien(sv);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công Sinh Viên");
            fillTable();
            resetFormSinhVien();
            btnMHCapNhat.setEnabled(false);
            btnMHXoa.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSVCapNhatActionPerformed

    private void btnSVLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVLuuActionPerformed
        // TODO add your handling code here:
        try {
            SinhVien sv = new SinhVien();
            sv.setStudentID(txtMaSV.getText());
            sv.setFullName(txtTenSV.getText());
            sv.setBirthDate(txtNgaySinh.getText());
            sv.setAddress(txtDiaChi.getText());
            sv.setGender(txtGioiTinh.getText());
            sv.setBranchID(txtBranchID.getText());

            SinhVienDAO dao = new SinhVienDAO(branchID);
            dao.addSinhVien(sv);
            JOptionPane.showMessageDialog(this, "Thêm thành công sinh viên");
            fillTable();
            resetFormSinhVien();
            btnLuu.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSVLuuActionPerformed

    private void btnSVLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSVLoadActionPerformed
        // TODO add your handling code here:
        fillTable();
        resetFormSinhVien();
    }//GEN-LAST:event_btnSVLoadActionPerformed

    private void btnCauHinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCauHinhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCauHinhActionPerformed

    private void btnCauHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCauHinhMouseClicked
        // TODO add your handling code here:
        LoginForm log = new LoginForm();
        log.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCauHinhMouseClicked

    private void cbxLopHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxLopHocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxLopHocActionPerformed

    private void tblDiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDiemMouseClicked
        // TODO add your handling code here:
        int row = tblDiem.getSelectedRow();
        if (row >= 0) {
            txtMaSVDiem.setText(tblDiem.getValueAt(row, 0).toString());
            txtTenSVDiem.setText(tblDiem.getValueAt(row, 1).toString());
            txtMidterm.setText(tblDiem.getValueAt(row, 2).toString());
            txtFinal.setText(tblDiem.getValueAt(row, 3).toString());
            txtAttendance.setText(tblDiem.getValueAt(row, 4).toString());
            btnMHCapNhat.setEnabled(true);
            btnMHXoa.setEnabled(true);
        }
    }//GEN-LAST:event_tblDiemMouseClicked

    private void btnDiemLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiemLuuActionPerformed
        // TODO add your handling code here:
        try {
            Diem diem = new Diem();
            diem.setStudentID(txtMaSVDiem.getText());
            diem.setFullName(txtTenSVDiem.getText());
            diem.setClassID((String) cbxLopHoc.getSelectedItem());
            diem.setMidtermScore(Float.parseFloat(txtMidterm.getText()));
            diem.setFinalScore(Float.parseFloat(txtFinal.getText()));
            diem.setAttendanceScore(Float.parseFloat(txtAttendance.getText()));

            DiemDAO dao = new DiemDAO(branchID);
            dao.updateDiem(diem);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công điểm sinh viên");
            updateTableWithClassID((String) cbxLopHoc.getSelectedItem());
            resetFormDiem();
            btnDiemLuu.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDiemLuuActionPerformed

    private void btnDiemCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiemCapNhatActionPerformed
        // TODO add your handling code here:
        btnDiemLuu.setEnabled(true);
    }//GEN-LAST:event_btnDiemCapNhatActionPerformed

    private void txtMidtermActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMidtermActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMidtermActionPerformed

    private void txtCourseIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCourseIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCourseIDActionPerformed

    private void btnDKDangKyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDKDangKyActionPerformed
        // TODO add your handling code here:
        try {

            DangKy dk = new DangKy();
            dk.setClassID((String) cbxDKMaLop.getSelectedItem());
            dk.setStudentID(txtDKMaSV.getText());

            DangKyDAO dao = new DangKyDAO(branchID);
            dao.addDangKy(dk);
            JOptionPane.showMessageDialog(this, "Đăng ký lớp học thành công");
            fillTableDK(txtDKMaSV.getText());
            btnDKDangKy.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDKDangKyActionPerformed

    private void btnDKLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDKLoadActionPerformed
        // TODO add your handling code here:
        btnDKDangKy.setEnabled(true);
        fillTableDK(txtDKMaSV.getText());
    }//GEN-LAST:event_btnDKLoadActionPerformed

    private void tblLopHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLopHocMouseClicked
        // TODO add your handling code here:
        int row = tblLopHoc.getSelectedRow(); // Lấy chỉ số của hàng được chọn
        if (row >= 0) {
            // Lấy giá trị từ các cột của bảng
            String classID = tblLopHoc.getValueAt(row, 0).toString();
            String className = tblLopHoc.getValueAt(row, 1).toString();
            String courseID = tblLopHoc.getValueAt(row, 2).toString();

            // Cập nhật các trường văn bản
            txtLHMaLop.setText(classID);
            txtLHTenLop.setText(className);

            txtLHChiNhanh.setText(tblLopHoc.getValueAt(row, 3).toString());

            // Tìm và chọn đối tượng MonHoc trong cbxMaMonHoc
            for (int i = 0; i < cbModelMH.getSize(); i++) {
                MonHoc mh = (MonHoc) cbModelMH.getElementAt(i); // Lấy đối tượng MonHoc từ model
                if (mh.getCourseID().equals(courseID)) {
                    cbxMaMonHoc.setSelectedItem(mh); // Đặt đối tượng được chọn
                    break;
                }
            }
            btnLHXoa.setEnabled(true);

        }

    }//GEN-LAST:event_tblLopHocMouseClicked

    private void btnLHSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLHSuaActionPerformed
        // TODO add your handling code here:
        try {
            // Khởi tạo đối tượng LopHoc
            LopHoc lh = new LopHoc();
            lh.setClassID(txtLHMaLop.getText()); // Đặt ClassID từ txtLHMaLop
            lh.setClassName(txtLHTenLop.getText()); // Đặt ClassName từ txtLHTenLop
            lh.setBranchID(txtLHChiNhanh.getText()); // Đặt BranchID từ txtLHChiNhanh

            // Lấy CourseID từ đối tượng được chọn trong ComboBox
            MonHoc selectedCourse = (MonHoc) cbxMaMonHoc.getSelectedItem();
            if (selectedCourse != null) {
                lh.setCourseID(selectedCourse.getCourseID());
            } else {
                throw new Exception("Vui lòng chọn môn học hợp lệ.");
            }

            // Khởi tạo DAO và thực hiện kiểm tra
            LopHocDAO dao = new LopHocDAO(branchID);
            // Thực hiện cập nhật
            if (dao.updateClass(lh)) {
                JOptionPane.showMessageDialog(this, "Update thành công lớp học");

                // Cập nhật lại bảng và reset form
                fillTableLopHoc();
                resetFormLopHoc();
                btnLHLuu.setEnabled(false); // Tắt nút "Lưu"
            } else {
                JOptionPane.showMessageDialog(this, "Không thể update lớp học. Vui lòng thử lại.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnLHSuaActionPerformed

    private void btnLHThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLHThemActionPerformed
        // TODO add your handling code here:
        resetFormLopHoc();
        btnLHLuu.setEnabled(true);

    }//GEN-LAST:event_btnLHThemActionPerformed

    private void btnLHXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLHXoaActionPerformed
        // TODO add your handling code here:
        try {
            if (!txtLHMaLop.getText().equals("")) {
                LopHocDAO dao = new LopHocDAO(branchID);
                if (dao.deleteClass(txtLHMaLop.getText())) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công Lớp Học");
                    fillTableLopHoc();
                    resetFormLopHoc();
                    btnLHLuu.setEnabled(false);
                    btnLHXoa.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa Lớp Học này này");
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLHXoaActionPerformed

    private void btnLHLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLHLuuActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        try {
            LopHoc lh = new LopHoc();
            lh.setClassID(txtLHMaLop.getText()); // Đặt ClassID từ txtLHMaLop
            lh.setClassName(txtLHTenLop.getText()); // Đặt ClassName từ txtLHTenLop
            lh.setBranchID(txtLHChiNhanh.getText()); // Đặt BranchID từ txtLHChiNhanh

            // Lấy CourseID từ đối tượng được chọn trong ComboBox
            MonHoc selectedCourse = (MonHoc) cbxMaMonHoc.getSelectedItem();
            if (selectedCourse != null) {
                lh.setCourseID(selectedCourse.getCourseID());
            } else {
                throw new Exception("Vui lòng chọn môn học hợp lệ.");
            }

            LopHocDAO dao = new LopHocDAO(branchID);
            if (dao.addClass(lh)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công lớp học mới");

                // Cập nhật lại bảng và reset form
                fillTableLopHoc();
                resetFormLopHoc();
                btnLHLuu.setEnabled(false); // Tắt nút "Lưu"
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm lớp học. Mã lớp đã tồn tại.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLHLuuActionPerformed

    private void cbxDKMaLopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxDKMaLopActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxDKMaLopActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu btnCauHinh;
    private javax.swing.JButton btnDKDangKy;
    private javax.swing.JButton btnDKLoad;
    private javax.swing.JButton btnDiemCapNhat;
    private javax.swing.JButton btnDiemLuu;
    private javax.swing.JButton btnLHLuu;
    private javax.swing.JButton btnLHSua;
    private javax.swing.JButton btnLHThem;
    private javax.swing.JButton btnLHXoa;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMHCapNhat;
    private javax.swing.JButton btnMHLoad;
    private javax.swing.JButton btnMHThem;
    private javax.swing.JButton btnMHXoa;
    private javax.swing.JButton btnSVCapNhat;
    private javax.swing.JButton btnSVLoad;
    private javax.swing.JButton btnSVLuu;
    private javax.swing.JButton btnSVThem;
    private javax.swing.JButton btnSVXoa;
    private javax.swing.JComboBox<String> cbxDKMaLop;
    private javax.swing.JComboBox<String> cbxLopHoc;
    private javax.swing.JComboBox<String> cbxMaMonHoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JTable tblDangKy;
    private javax.swing.JTable tblDiem;
    private javax.swing.JTable tblLopHoc;
    private javax.swing.JTable tblMonHoc;
    private javax.swing.JTable tblSinhVien;
    private javax.swing.JTextField txtAttendance;
    private javax.swing.JTextField txtBranchID;
    private javax.swing.JTextField txtChiNhanh;
    private javax.swing.JTextField txtCourseID;
    private javax.swing.JTextField txtCourseName;
    private javax.swing.JTextField txtCredits;
    private javax.swing.JTextField txtDKMaSV;
    private javax.swing.JTextField txtDKSV;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDiemGradeID;
    private javax.swing.JTextField txtFinal;
    private javax.swing.JTextField txtGioiTinh;
    private javax.swing.JTextField txtLHChiNhanh;
    private javax.swing.JTextField txtLHMaLop;
    private javax.swing.JTextField txtLHTenLop;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtMaSVDiem;
    private javax.swing.JTextField txtMidterm;
    private javax.swing.JTextField txtNgaySinh;
    private javax.swing.JTextField txtTenSV;
    private javax.swing.JTextField txtTenSVDiem;
    // End of variables declaration//GEN-END:variables
}
