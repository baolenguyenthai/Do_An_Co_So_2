package view;

import dao.BaiThiDAO;
import dao.CapHocDAO;
import dao.MonHocDAO;
import model.CapHoc;
import model.MonHoc;
import java.io.File;
import java.io.FileOutputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class pnlThongKeBaoCao extends javax.swing.JPanel {

        private final BaiThiDAO baiThiDAO = new BaiThiDAO();
        private final MonHocDAO monHocDAO = new MonHocDAO();
        private final CapHocDAO capHocDAO = new CapHocDAO();
        private DefaultTableModel summaryModel;
        private DefaultTableModel detailModel;
        private TableRowSorter<DefaultTableModel> summarySorter;

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
                summarySorter = new TableRowSorter<>(summaryModel);
                tblThongKe.setRowSorter(summarySorter);

                // Click on summary table to filter detailed table
                tblThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                int viewRow = tblThongKe.getSelectedRow();
                                if (viewRow != -1) {
                                        int modelRow = tblThongKe.convertRowIndexToModel(viewRow);
                                        String subject = summaryModel.getValueAt(modelRow, 0).toString();
                                        cbFilterMonHoc.setSelectedItem(subject);
                                        loadDetailData();
                                }
                        }
                });

                // Table 2: Detailed Results
                detailModel = new DefaultTableModel(
                                new Object[][] {},
                                new String[] { "Hạng", "Thí sinh", "Môn học", "Cấp học", "Điểm", "Đúng/Tổng",
                                                "Thời gian làm bài", "Ngày nộp", "Mã bài thi", "Bộ câu hỏi" }) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblChiTiet.setModel(detailModel);
                tblChiTiet.setRowHeight(30);
                hideDetailMetaColumns();
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
                applySummaryFilter();

                loadDetailData();
        }

        public void loadDetailData() {
                String name = txtFilterName.getText().trim();
                String subject = (String) cbFilterMonHoc.getSelectedItem();
                String grade = (String) cbFilterCapHoc.getSelectedItem();

                List<Object[]> data = baiThiDAO.getBangXepHangCoThoiGianLamBai(name, subject, grade);
                detailModel.setRowCount(0);
                for (Object[] row : data) {
                        int thoiLuongGiay = 0;
                        if (row[6] instanceof Number) {
                                thoiLuongGiay = ((Number) row[6]).intValue();
                        }
                        String thoiGianLamBai = dinhDangThoiLuongGiay(thoiLuongGiay);
                        detailModel.addRow(new Object[] {
                                        row[0],
                                        row[1],
                                        row[2],
                                        row[3],
                                        row[4],
                                        row[5],
                                        thoiGianLamBai,
                                        row[7],
                                        row[8],
                                        row[9]
                        });
                }
                hideDetailMetaColumns();
        }

        private void applySummaryFilter() {
                if (summarySorter == null) {
                        return;
                }
                String keyword = txtSearchMonHoc.getText().trim();
                if (keyword.isEmpty()) {
                        summarySorter.setRowFilter(null);
                        return;
                }
                summarySorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(keyword), 0));
        }

        private void hideDetailMetaColumns() {
                if (tblChiTiet.getColumnModel().getColumnCount() < 10) {
                        return;
                }
                tblChiTiet.getColumnModel().getColumn(8).setMinWidth(0);
                tblChiTiet.getColumnModel().getColumn(8).setMaxWidth(0);
                tblChiTiet.getColumnModel().getColumn(8).setPreferredWidth(0);

                tblChiTiet.getColumnModel().getColumn(9).setMinWidth(0);
                tblChiTiet.getColumnModel().getColumn(9).setMaxWidth(0);
                tblChiTiet.getColumnModel().getColumn(9).setPreferredWidth(0);
        }

        private String dinhDangThoiLuongGiay(int thoiLuongGiay) {
                if (thoiLuongGiay <= 0) {
                        return "0:00";
                }
                int gio = thoiLuongGiay / 3600;
                int phut = (thoiLuongGiay % 3600) / 60;
                int giay = thoiLuongGiay % 60;
                if (gio > 0) {
                        return String.format(Locale.ROOT, "%d:%02d:%02d", gio, phut, giay);
                }
                return String.format(Locale.ROOT, "%d:%02d", phut, giay);
        }

        private void xemChiTietBaiThi() {
                int selectedRow = tblChiTiet.getSelectedRow();
                if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng trong bảng chi tiết.",
                                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int modelRow = tblChiTiet.convertRowIndexToModel(selectedRow);
                Object baiThiIdObj = detailModel.getValueAt(modelRow, 8);
                if (!(baiThiIdObj instanceof Number)) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy mã bài thi để xem chi tiết.",
                                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int baiThiId = ((Number) baiThiIdObj).intValue();
                Object tenBoCauHoiObj = detailModel.getValueAt(modelRow, 9);
                String tenBoCauHoi = tenBoCauHoiObj != null ? tenBoCauHoiObj.toString() : "Bài thi " + baiThiId;

                FrmXemChiTietBaiThi frm = new FrmXemChiTietBaiThi(baiThiId, tenBoCauHoi);
                frm.setVisible(true);
        }

        private void xuatDanhSachChiTietRaExcel() {
                if (detailModel == null || detailModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất Excel.", "Thông báo",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                String monHoc = (String) cbFilterMonHoc.getSelectedItem();
                String capHoc = (String) cbFilterCapHoc.getSelectedItem();
                String tenNguoiDung = txtFilterName.getText().trim();
                String thongTinLoc = "Tìm tên: " + (tenNguoiDung.isEmpty() ? "Tất cả" : tenNguoiDung)
                                + " | Môn học: " + (monHoc == null ? "Tất cả" : monHoc)
                                + " | Cấp học: " + (capHoc == null ? "Tất cả" : capHoc);

                JFileChooser boChonTep = new JFileChooser();
                boChonTep.setDialogTitle("Chọn nơi lưu file Excel");
                String tenFileMacDinh = taoTenTepExcelMacDinh(monHoc);
                boChonTep.setSelectedFile(new File(tenFileMacDinh));
                int ketQua = boChonTep.showSaveDialog(this);
                if (ketQua != JFileChooser.APPROVE_OPTION) {
                        return;
                }

                File tep = boChonTep.getSelectedFile();
                if (tep != null && !tep.getName().toLowerCase(Locale.ROOT).endsWith(".xlsx")) {
                        tep = new File(tep.getParentFile(), tep.getName() + ".xlsx");
                }

                try (Workbook soLamViec = new XSSFWorkbook()) {
                        Sheet bang = soLamViec.createSheet("BaoCaoChiTiet");

                        Font chuTieuDe = soLamViec.createFont();
                        chuTieuDe.setBold(true);
                        chuTieuDe.setFontHeightInPoints((short) 16);

                        CellStyle kieuTieuDe = soLamViec.createCellStyle();
                        kieuTieuDe.setFont(chuTieuDe);
                        kieuTieuDe.setAlignment(HorizontalAlignment.CENTER);
                        kieuTieuDe.setVerticalAlignment(VerticalAlignment.CENTER);

                        Font chuThongTinLoc = soLamViec.createFont();
                        chuThongTinLoc.setItalic(true);

                        CellStyle kieuThongTinLoc = soLamViec.createCellStyle();
                        kieuThongTinLoc.setFont(chuThongTinLoc);
                        kieuThongTinLoc.setAlignment(HorizontalAlignment.CENTER);
                        kieuThongTinLoc.setVerticalAlignment(VerticalAlignment.CENTER);

                        Font chuTieuDeCot = soLamViec.createFont();
                        chuTieuDeCot.setBold(true);

                        CellStyle kieuDuLieu = soLamViec.createCellStyle();
                        kieuDuLieu.setBorderTop(BorderStyle.THIN);
                        kieuDuLieu.setBorderBottom(BorderStyle.THIN);
                        kieuDuLieu.setBorderLeft(BorderStyle.THIN);
                        kieuDuLieu.setBorderRight(BorderStyle.THIN);
                        kieuDuLieu.setVerticalAlignment(VerticalAlignment.CENTER);

                        CellStyle kieuTieuDeCot = soLamViec.createCellStyle();
                        kieuTieuDeCot.cloneStyleFrom(kieuDuLieu);
                        kieuTieuDeCot.setFont(chuTieuDeCot);
                        kieuTieuDeCot.setAlignment(HorizontalAlignment.CENTER);
                        kieuTieuDeCot.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                        kieuTieuDeCot.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                        CreationHelper taoDinhDang = soLamViec.getCreationHelper();
                        CellStyle kieuNgayGio = soLamViec.createCellStyle();
                        kieuNgayGio.cloneStyleFrom(kieuDuLieu);
                        kieuNgayGio.setDataFormat(taoDinhDang.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));

                        int soCotXuat = Math.min(8, detailModel.getColumnCount());

                        int dong = 0;
                        bang.createRow(dong++); // để trống dòng 1 như mẫu

                        Row rowTieuDe = bang.createRow(dong++);
                        rowTieuDe.setHeightInPoints(22);
                        Cell cellTieuDe = rowTieuDe.createCell(0);
                        cellTieuDe.setCellValue("BÁO CÁO CHI TIẾT BẢNG XẾP HẠNG");
                        cellTieuDe.setCellStyle(kieuTieuDe);
                        if (soCotXuat > 1) {
                                bang.addMergedRegion(new CellRangeAddress(rowTieuDe.getRowNum(), rowTieuDe.getRowNum(),
                                                0, soCotXuat - 1));
                        }

                        Row rowLoc = bang.createRow(dong++);
                        Cell cellLoc = rowLoc.createCell(0);
                        cellLoc.setCellValue(thongTinLoc);
                        cellLoc.setCellStyle(kieuThongTinLoc);
                        if (soCotXuat > 1) {
                                bang.addMergedRegion(
                                                new CellRangeAddress(rowLoc.getRowNum(), rowLoc.getRowNum(), 0,
                                                                soCotXuat - 1));
                        }

                        Row rowHeader = bang.createRow(dong++);
                        rowHeader.setHeightInPoints(18);
                        for (int c = 0; c < soCotXuat; c++) {
                                Cell cell = rowHeader.createCell(c);
                                cell.setCellValue(detailModel.getColumnName(c));
                                cell.setCellStyle(kieuTieuDeCot);
                        }

                        for (int r = 0; r < detailModel.getRowCount(); r++) {
                                Row row = bang.createRow(dong++);
                                for (int c = 0; c < soCotXuat; c++) {
                                        Object giaTri = detailModel.getValueAt(r, c);
                                        Cell cell = row.createCell(c);
                                        if (giaTri == null) {
                                                cell.setCellValue("");
                                                cell.setCellStyle(kieuDuLieu);
                                                continue;
                                        }
                                        if (giaTri instanceof Number) {
                                                cell.setCellValue(((Number) giaTri).doubleValue());
                                                cell.setCellStyle(kieuDuLieu);
                                                continue;
                                        }
                                        if (giaTri instanceof Date) {
                                                cell.setCellValue((Date) giaTri);
                                                cell.setCellStyle(kieuNgayGio);
                                                continue;
                                        }
                                        cell.setCellValue(giaTri.toString());
                                        cell.setCellStyle(kieuDuLieu);
                                }
                        }

                        bang.createFreezePane(0, 4);
                        for (int c = 0; c < soCotXuat; c++) {
                                bang.autoSizeColumn(c);
                        }

                        try (FileOutputStream fos = new FileOutputStream(tep)) {
                                soLamViec.write(fos);
                        }

                        JOptionPane.showMessageDialog(this, "Xuất Excel thành công:\n" + tep.getAbsolutePath(),
                                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Xuất Excel thất bại: " + e.getMessage(), "Lỗi",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        private String taoTenTepExcelMacDinh(String monHoc) {
                String tenMonHoc = (monHoc == null || monHoc.trim().isEmpty() || "Tất cả".equals(monHoc))
                                ? "tat_ca"
                                : chuyenChuoiThanhTenTep(monHoc);
                String thoiGian = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(new Date());
                return "bao_cao_chi_tiet_" + tenMonHoc + "_" + thoiGian + ".xlsx";
        }

        private String chuyenChuoiThanhTenTep(String giaTri) {
                String khongDau = Normalizer.normalize(giaTri, Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                khongDau = khongDau.replace('đ', 'd').replace('Đ', 'D');
                String daLoc = Pattern.compile("[^a-zA-Z0-9]+").matcher(khongDau).replaceAll("_");
                daLoc = daLoc.replaceAll("^_+|_+$", "");
                if (daLoc.isEmpty()) {
                        return "tep";
                }
                return daLoc.toLowerCase(Locale.ROOT);
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
                pnlSummaryFilter = new javax.swing.JPanel();
                jLabelSummarySearch = new javax.swing.JLabel();
                txtSearchMonHoc = new javax.swing.JTextField();
                btnSearchMonHoc = new javax.swing.JButton();
                btnClearSearchMonHoc = new javax.swing.JButton();
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
                btnXemChiTiet = new javax.swing.JButton();
                btnXuatExcel = new javax.swing.JButton();
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

                pnlSummaryFilter.setBackground(new java.awt.Color(255, 255, 255));

                jLabelSummarySearch.setText("Tìm môn học:");

                btnSearchMonHoc.setText("Tìm kiếm");
                btnSearchMonHoc.addActionListener(e -> applySummaryFilter());

                btnClearSearchMonHoc.setText("Xóa lọc");
                btnClearSearchMonHoc.addActionListener(e -> {
                        txtSearchMonHoc.setText("");
                        applySummaryFilter();
                });

                txtSearchMonHoc.addActionListener(e -> applySummaryFilter());

                javax.swing.GroupLayout pnlSummaryFilterLayout = new javax.swing.GroupLayout(pnlSummaryFilter);
                pnlSummaryFilter.setLayout(pnlSummaryFilterLayout);
                pnlSummaryFilterLayout.setHorizontalGroup(
                                pnlSummaryFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnlSummaryFilterLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabelSummarySearch)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtSearchMonHoc,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                220,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(12, 12, 12)
                                                                .addComponent(btnSearchMonHoc,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                100,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(8, 8, 8)
                                                                .addComponent(btnClearSearchMonHoc,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                100,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                pnlSummaryFilterLayout.setVerticalGroup(
                                pnlSummaryFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnlSummaryFilterLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(pnlSummaryFilterLayout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabelSummarySearch)
                                                                                .addComponent(txtSearchMonHoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnSearchMonHoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnClearSearchMonHoc,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap()));

                pnlDetails.setBackground(new java.awt.Color(255, 255, 255));
                pnlDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Báo cáo chi tiết"));

                jLabelDetails1.setText("Tìm tên:");
                jLabelDetails2.setText("Môn học:");
                jLabelDetails3.setText("Cấp học:");

                btnFilter.setText("Lọc dữ liệu");
                btnFilter.addActionListener(e -> loadDetailData());

                btnXemChiTiet.setText("Xem chi tiết");
                btnXemChiTiet.addActionListener(e -> xemChiTietBaiThi());

                btnXuatExcel.setText("Xuất Excel");
                btnXuatExcel.setBackground(new java.awt.Color(0, 153, 0));
                btnXuatExcel.setForeground(new java.awt.Color(255, 255, 255));
                btnXuatExcel.setOpaque(true);
                btnXuatExcel.setBorderPainted(false);
                btnXuatExcel.addActionListener(e -> xuatDanhSachChiTietRaExcel());

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
                                                                                                .addGap(8, 8, 8)
                                                                                                .addComponent(btnXemChiTiet,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                120,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(8, 8, 8)
                                                                                                .addComponent(btnXuatExcel,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                110,
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
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnXemChiTiet,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                30,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnXuatExcel,
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
                                                                                .addComponent(pnlSummaryFilter,
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
                                                                .addComponent(pnlSummaryFilter,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10, 10, 10)
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
        private javax.swing.JButton btnClearSearchMonHoc;
        private javax.swing.JButton btnSearchMonHoc;
        private javax.swing.JButton btnXemChiTiet;
        private javax.swing.JButton btnXuatExcel;
        private javax.swing.JComboBox<String> cbFilterCapHoc;
        private javax.swing.JComboBox<String> cbFilterMonHoc;
        private javax.swing.JLabel jLabelDetails1;
        private javax.swing.JLabel jLabelDetails2;
        private javax.swing.JLabel jLabelDetails3;
        private javax.swing.JLabel jLabelSummarySearch;
        private javax.swing.JPanel pnlOverview;
        private javax.swing.JPanel pnlSummaryFilter;
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
        private javax.swing.JTextField txtSearchMonHoc;
        private javax.swing.JPanel pnlDetails;
}
