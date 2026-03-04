package view;

import dao.CapHocDAO;
import dao.CauHoiDAO;
import dao.DapAnDAO;
import dao.MonHocDAO;
import model.CapHoc;
import model.CauHoi;
import model.DapAn;
import model.MonHoc;
import utils.QuanLyPhien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmCauHoiYeuThich extends javax.swing.JFrame {

    private CauHoiDAO cauHoiDAO = new CauHoiDAO();
    private MonHocDAO monHocDAO = new MonHocDAO();
    private CapHocDAO capHocDAO = new CapHocDAO();
    private DapAnDAO dapAnDAO = new DapAnDAO();

    private DefaultTableModel tableModel;

    public FrmCauHoiYeuThich() {
        initComponents();
        setLocationRelativeTo(null);
        setupTable();
        loadFilters();
        loadData();
    }

    private void setupTable() {
        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Nội dung", "Mức độ", "Đáp án đúng" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCauHoi.setModel(tableModel);
        tblCauHoi.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblCauHoi.getColumnModel().getColumn(1).setPreferredWidth(400);
        tblCauHoi.setRowHeight(30);
    }

    private void loadFilters() {
        cbMonHoc.removeAllItems();
        cbMonHoc.addItem("Tất cả");
        for (MonHoc m : monHocDAO.getAll()) {
            cbMonHoc.addItem(m.getTenMonHoc());
        }

        cbCapHoc.removeAllItems();
        cbCapHoc.addItem("Tất cả");
        for (CapHoc c : capHocDAO.getAll()) {
            cbCapHoc.addItem(c.getTenCapHoc());
        }
    }

    private void loadData() {
        String monHoc = (String) cbMonHoc.getSelectedItem();
        String capHoc = (String) cbCapHoc.getSelectedItem();
        int userId = QuanLyPhien.layIdNguoiDungHienTai();

        List<CauHoi> list = cauHoiDAO.getFavoriteQuestions(userId, monHoc, capHoc);
        tableModel.setRowCount(0);
        for (CauHoi ch : list) {
            List<DapAn> suggestions = dapAnDAO.getByCauHoiId(ch.getCauHoiId());
            String correctAns = "Chưa có";
            for (DapAn da : suggestions) {
                if (da.getDung() == 1) {
                    correctAns = da.getNoiDung();
                    break;
                }
            }
            tableModel.addRow(new Object[] {
                    ch.getCauHoiId(),
                    ch.getNoiDung(),
                    ch.getMucDo(),
                    correctAns
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbMonHoc = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbCapHoc = new javax.swing.JComboBox<>();
        btnLoc = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCauHoi = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        btnXoaYeuThich = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Câu hỏi yêu thích");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24));
        jLabel1.setText("CÂU HỎI YÊU THÍCH");

        jLabel2.setText("Môn học:");
        jLabel3.setText("Cấp học:");

        btnLoc.setText("Lọc nhanh");
        btnLoc.addActionListener(e -> loadData());

        tblCauHoi.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {}));
        jScrollPane1.setViewportView(tblCauHoi);

        btnBack.setText("Quay lại");
        btnBack.addActionListener(e -> this.dispose());

        btnXoaYeuThich.setText("Bỏ yêu thích");
        btnXoaYeuThich.addActionListener(e -> {
            int row = tblCauHoi.getSelectedRow();
            if (row != -1) {
                int chId = (int) tblCauHoi.getValueAt(row, 0);
                int userId = QuanLyPhien.layIdNguoiDungHienTai();
                if (cauHoiDAO.toggleFavorite(userId, chId)) {
                    loadData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn câu hỏi cần xóa!");
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760,
                                                Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cbMonHoc,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cbCapHoc,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(btnLoc)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btnBack)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnXoaYeuThich)))
                                .addGap(20, 20, 20)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1)
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(cbMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(cbCapHoc, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnLoc))
                                .addGap(20, 20, 20)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnBack)
                                        .addComponent(btnXoaYeuThich))
                                .addGap(20, 20, 20)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmCauHoiYeuThich().setVisible(true));
    }

    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnLoc;
    private javax.swing.JButton btnXoaYeuThich;
    private javax.swing.JComboBox<String> cbCapHoc;
    private javax.swing.JComboBox<String> cbMonHoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCauHoi;
}
