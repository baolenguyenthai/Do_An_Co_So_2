package view;

import dao.BaiThiDAO;
import dao.CapHocDAO;
import dao.MonHocDAO;
import model.CapHoc;
import model.MonHoc;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class pnlThongKeBaoCao extends javax.swing.JPanel {

        private final BaiThiDAO baiThiDAO = new BaiThiDAO();
        private final MonHocDAO monHocDAO = new MonHocDAO();
        private final CapHocDAO capHocDAO = new CapHocDAO();
        private DefaultTableModel summaryModel;
        private DefaultTableModel detailModel;

        public pnlThongKeBaoCao() {
                initComponents();
                setupTables();
                loadFilters();
                loadData();
        }

        private void setupTables() {
                // Table 1: Summary by Subject
                summaryModel = new DefaultTableModel(
                                new Object[][] {},
                                new String[] { "Tên môn học", "Số lượt thi", "Điểm trung bình" }) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblThongKe.setModel(summaryModel);
                tblThongKe.setRowHeight(30);

                // Click on summary table to filter detailed table
                tblThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                int row = tblThongKe.getSelectedRow();
                                if (row != -1) {
                                        String subject = summaryModel.getValueAt(row, 0).toString();
                                        cbFilterMonHoc.setSelectedItem(subject);
                                        loadDetailData();
                                }
                        }
                });

                // Table 2: Detailed Results
                detailModel = new DefaultTableModel(
                                new Object[][] {},
                                new String[] { "Hạng", "Thí sinh", "Môn học", "Cấp học", "Điểm", "Đúng/Tổng",
                                                "Ngày nộp" }) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblChiTiet.setModel(detailModel);
                tblChiTiet.setRowHeight(30);
        }

        private void loadFilters() {
                cbFilterMonHoc.removeAllItems();
                cbFilterMonHoc.addItem("Tất cả");
                for (MonHoc m : monHocDAO.getAll()) {
                        cbFilterMonHoc.addItem(m.getTenMonHoc());
                }

                cbFilterCapHoc.removeAllItems();
                cbFilterCapHoc.addItem("Tất cả");
                for (CapHoc c : capHocDAO.getAll()) {
                        cbFilterCapHoc.addItem(c.getTenCapHoc());
                }
        }

        public void loadData() {
                // Load overview stats
                Object[] stats = baiThiDAO.getStatistics();
                lblTotalExams.setText(stats[0].toString());
                lblAvgScore.setText(String.format("%.2f", (Float) stats[1]));
                lblTopUser.setText(stats[2] != null ? stats[2].toString() : "N/A");
                lblTopSubject.setText(stats[3] != null ? stats[3].toString() : "N/A");

                // Load summary table
                List<Object[]> subjectStats = baiThiDAO.getStatisticsBySubject();
                summaryModel.setRowCount(0);
                for (Object[] row : subjectStats) {
                        summaryModel.addRow(row);
                }

                loadDetailData();
        }

        public void loadDetailData() {
                String name = txtFilterName.getText().trim();
                String subject = (String) cbFilterMonHoc.getSelectedItem();
                String grade = (String) cbFilterCapHoc.getSelectedItem();

                List<Object[]> data = baiThiDAO.getBangXepHang(name, subject, grade);
                detailModel.setRowCount(0);
                for (Object[] row : data) {
                        detailModel.addRow(row);
                }
        }

        @SuppressWarnings("unchecked")
        private void initComponents() {
                pnlOverview = new javax.swing.JPanel();
                card1 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                lblTotalExams = new javax.swing.JLabel();
                card2 = new javax.swing.JPanel();
                jLabel3 = new javax.swing.JLabel();
                lblAvgScore = new javax.swing.JLabel();
                card3 = new javax.swing.JPanel();
                jLabel5 = new javax.swing.JLabel();
                lblTopUser = new javax.swing.JLabel();
                card4 = new javax.swing.JPanel();
                jLabel7 = new javax.swing.JLabel();
                lblTopSubject = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                tblThongKe = new javax.swing.JTable();
                lblHeader = new javax.swing.JLabel();
                pnlDetails = new javax.swing.JPanel();
                jLabelDetails1 = new javax.swing.JLabel();
                txtFilterName = new javax.swing.JTextField();
                jLabelDetails2 = new javax.swing.JLabel();
                cbFilterMonHoc = new javax.swing.JComboBox<>();
                jLabelDetails3 = new javax.swing.JLabel();
                cbFilterCapHoc = new javax.swing.JComboBox<>();
                btnFilter = new javax.swing.JButton();
                jScrollPane2 = new javax.swing.JScrollPane();
                tblChiTiet = new javax.swing.JTable();

                setBackground(new java.awt.Color(245, 245, 250));

                pnlOverview.setOpaque(false);
                pnlOverview.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

                card1.setBackground(new java.awt.Color(255, 255, 255));
                card1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)));
                card1.setLayout(new java.awt.GridLayout(2, 1));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("Tổng số lượt thi");
                card1.add(jLabel1);
                lblTotalExams.setFont(new java.awt.Font("Helvetica Neue", 1, 24));
                lblTotalExams.setForeground(new java.awt.Color(102, 102, 255));
                lblTotalExams.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblTotalExams.setText("0");
                card1.add(lblTotalExams);
                pnlOverview.add(card1);

                card2.setBackground(new java.awt.Color(255, 255, 255));
                card2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)));
                card2.setLayout(new java.awt.GridLayout(2, 1));
                jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel3.setText("Điểm TB hệ thống");
                card2.add(jLabel3);
                lblAvgScore.setFont(new java.awt.Font("Helvetica Neue", 1, 24));
                lblAvgScore.setForeground(new java.awt.Color(0, 204, 102));
                lblAvgScore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblAvgScore.setText("0.0");
                card2.add(lblAvgScore);
                pnlOverview.add(card2);

                card3.setBackground(new java.awt.Color(255, 255, 255));
                card3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)));
                card3.setLayout(new java.awt.GridLayout(2, 1));
                jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel5.setText("Người thi tích cực nhất");
                card3.add(jLabel5);
                lblTopUser.setFont(new java.awt.Font("Helvetica Neue", 1, 16));
                lblTopUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblTopUser.setText("N/A");
                card3.add(lblTopUser);
                pnlOverview.add(card3);

                card4.setBackground(new java.awt.Color(255, 255, 255));
                card4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)));
                card4.setLayout(new java.awt.GridLayout(2, 1));
                jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel7.setText("Môn học hot nhất");
                card4.add(jLabel7);
                lblTopSubject.setFont(new java.awt.Font("Helvetica Neue", 1, 16));
                lblTopSubject.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                lblTopSubject.setText("N/A");
                card4.add(lblTopSubject);
                pnlOverview.add(card4);

                tblThongKe.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] {}));
                jScrollPane1.setViewportView(tblThongKe);

                lblHeader.setFont(new java.awt.Font("Helvetica Neue", 1, 26));
                lblHeader.setText("THỐNG KÊ VÀ BÁO CÁO HỆ THỐNG");

                pnlDetails.setBackground(new java.awt.Color(255, 255, 255));
                pnlDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Báo cáo chi tiết"));

                jLabelDetails1.setText("Tìm tên:");
                jLabelDetails2.setText("Môn học:");
                jLabelDetails3.setText("Cấp học:");

                btnFilter.setText("Lọc dữ liệu");
                btnFilter.addActionListener(e -> loadDetailData());

                tblChiTiet.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] {}));
                jScrollPane2.setViewportView(tblChiTiet);

                javax.swing.GroupLayout pnlDetailsLayout = new javax.swing.GroupLayout(pnlDetails);
                pnlDetails.setLayout(pnlDetailsLayout);
                pnlDetailsLayout.setHorizontalGroup(
                                pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnlDetailsLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(pnlDetailsLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jScrollPane2)
                                                                                .addGroup(pnlDetailsLayout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabelDetails1)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(txtFilterName,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                150,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(jLabelDetails2)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(cbFilterMonHoc,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                150,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(jLabelDetails3)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(cbFilterCapHoc,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                150,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(btnFilter,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                100,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                                .addContainerGap()));
                pnlDetailsLayout.setVerticalGroup(
                                pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnlDetailsLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(pnlDetailsLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabelDetails1)
                                                                                .addComponent(txtFilterName,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabelDetails2)
                                                                                .addComponent(cbFilterMonHoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabelDetails3)
                                                                                .addComponent(cbFilterCapHoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnFilter,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jScrollPane2,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                300, Short.MAX_VALUE)
                                                                .addContainerGap()));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(25, 25, 25)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(lblHeader)
                                                                                .addComponent(pnlOverview,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                1000, Short.MAX_VALUE)
                                                                                .addComponent(pnlDetails,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(25, 25, 25)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(25, 25, 25)
                                                                .addComponent(lblHeader)
                                                                .addGap(30, 30, 30)
                                                                .addComponent(pnlOverview,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                100,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                150,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(20, 20, 20)
                                                                .addComponent(pnlDetails,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGap(25, 25, 25)));
        }

        private javax.swing.JButton btnFilter;
        private javax.swing.JComboBox<String> cbFilterCapHoc;
        private javax.swing.JComboBox<String> cbFilterMonHoc;
        private javax.swing.JLabel jLabelDetails1;
        private javax.swing.JLabel jLabelDetails2;
        private javax.swing.JLabel jLabelDetails3;
        private javax.swing.JPanel pnlOverview;
        private javax.swing.JPanel card1;
        private javax.swing.JPanel card2;
        private javax.swing.JPanel card3;
        private javax.swing.JPanel card4;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel lblAvgScore;
        private javax.swing.JLabel lblHeader;
        private javax.swing.JLabel lblTopSubject;
        private javax.swing.JLabel lblTopUser;
        private javax.swing.JLabel lblTotalExams;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JTable tblThongKe;
        private javax.swing.JTable tblChiTiet;
        private javax.swing.JTextField txtFilterName;
        private javax.swing.JPanel pnlDetails;
}
