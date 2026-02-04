
package view;
import dao.*;
import model.*;
import utils.QuanLyPhien;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.sql.Date;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
/**
 *
 * @author lebao
 */
public class FrmLamBaiThi extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmLamBaiThi.class.getName());
    private int maxSoCauHoi = 0;  // Số câu hỏi tối đa của bộ câu hỏi hiện tại
    private boolean isUpdatingCombo = false;
    // DAO instances
    private CapHocDAO capHocDAO = new CapHocDAO();
    private MonHocDAO monHocDAO = new MonHocDAO();
    private BoCauHoiDAO boCauHoiDAO = new BoCauHoiDAO();
    private CauHoiDAO cauHoiDAO = new CauHoiDAO();
    private DapAnDAO dapAnDAO = new DapAnDAO();
    private BaiThiDAO baiThiDAO = new BaiThiDAO();

    // Logic variables
    private List<CauHoi> danhSachCauHoi;
    private List<DapAn> danhSachDapAn;
    private Map<Integer, Integer> dapAnDaChon = new HashMap<>();
    private Map<Integer, List<DapAn>> thuTuDapAnDaTron = new HashMap<>(); // Lưu thứ tự đáp án đã trộn theo cauHoiId
    private int cauHienTai = 0;
    private Timer timer;
    private int thoiGianConLai;
    private int boCauHoiId;
    private Date thoiGianBatDau;

    // ButtonGroup cho đáp án
    private ButtonGroup bgDapAn = new ButtonGroup();
    
    /**
     * Creates new form FrmLamBaiThi
     */
    public FrmLamBaiThi() {
        initComponents();
        setLocationRelativeTo(null);
        taCauHoi.setEditable(false); // ko cho chỉnh
        
        // TỰ ĐỘNG XUỐNG DÒNG
        taCauHoi.setLineWrap(true);
        taCauHoi.setWrapStyleWord(true);

        // PADDING 8PX
        taCauHoi.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(new Color(153, 153, 255), 2, true),
                new EmptyBorder(8, 8, 8, 8)
            )
        ); 

        // Thêm ButtonGroup cho radio buttons
        bgDapAn.add(rbDapAn1);
        bgDapAn.add(rbDapAn2);
        bgDapAn.add(rbDapAn3);
        bgDapAn.add(rbDapAn4);

        // GẮN LISTENER LƯU ĐÁP ÁN NGAY KHI CLICK
        ActionListener luaChonDapAnListener = e -> luuDapAnDangChon();
        rbDapAn1.addActionListener(luaChonDapAnListener);
        rbDapAn2.addActionListener(luaChonDapAnListener);
        rbDapAn3.addActionListener(luaChonDapAnListener);
        rbDapAn4.addActionListener(luaChonDapAnListener);


        // Ẩn các component bài thi ban đầu
        
        lblCauHienTai.setVisible(false);
        jLabel6.setVisible(false);
        jLabel7.setVisible(false);
        jLabel8.setVisible(false);
        jLabel9.setVisible(false);
        jLabel10.setVisible(false);
        
        lblTimer.setVisible(false);
        rbDapAn1.setVisible(false);
        rbDapAn2.setVisible(false);
        rbDapAn3.setVisible(false);
        rbDapAn4.setVisible(false);
        btnCauTruoc.setVisible(false);
        btnTiepTheo.setVisible(false);
        btnNopBai.setVisible(false);

        loadCapHoc();
        loadMonHoc();
        setupUI();
    }
    
    private void setupUI() {
        cbCapHoc.addActionListener(e -> {
            if (isUpdatingCombo) return;
            loadMonHocTheoCapHoc(); // sẽ tự gọi loadBoCauHoi()
        });

        cbMonHoc.addActionListener(e -> {
            if (isUpdatingCombo) return;
            loadBoCauHoi();
            if (chkMaxCauHoi.isSelected() && maxSoCauHoi > 0) {
                spSoCauHoi.setValue(maxSoCauHoi);
            }
        });

        chkMaxCauHoi.addActionListener(e -> {
            if (chkMaxCauHoi.isSelected()) {
                if (maxSoCauHoi > 0) {
                    spSoCauHoi.setValue(maxSoCauHoi);
                } else {
                    JOptionPane.showMessageDialog(this, "Chưa có bộ câu hỏi cho môn học này!");
                    chkMaxCauHoi.setSelected(false);
                }
            } else {
                spSoCauHoi.setValue(1);
            }
        });
    }
    
    private void loadMonHocTheoCapHoc() {
        isUpdatingCombo = true;
        try {
            String tenCapHoc = (String) cbCapHoc.getSelectedItem();
            if (tenCapHoc != null) {
                CapHoc selectedCapHoc = capHocDAO.getAll().stream()
                    .filter(c -> c.getTenCapHoc().equals(tenCapHoc))
                    .findFirst().orElse(null);
                if (selectedCapHoc != null) {
                    int capHocId = selectedCapHoc.getCapHocId();
                    List<BoCauHoi> boCauHoiList = boCauHoiDAO.getByCapHoc(capHocId);

                    Set<Integer> monHocIdSet = new HashSet<>();
                    for (BoCauHoi b : boCauHoiList) {
                        monHocIdSet.add(b.getMonHocId());
                    }

                    cbMonHoc.removeAllItems();
                    for (Integer monHocId : monHocIdSet) {
                        MonHoc m = monHocDAO.getAll().stream()
                            .filter(mh -> mh.getMonHocId() == monHocId)
                            .findFirst().orElse(null);
                        if (m != null) cbMonHoc.addItem(m.getTenMonHoc());
                    }

                    if (cbMonHoc.getItemCount() > 0 && cbMonHoc.getSelectedIndex() == -1) {
                        cbMonHoc.setSelectedIndex(0);
                    }
                }
            }
        } finally {
            isUpdatingCombo = false;
        }

        loadBoCauHoi();
        if (chkMaxCauHoi.isSelected() && maxSoCauHoi > 0) {
            spSoCauHoi.setValue(maxSoCauHoi);
        }
    }
    
    private void setCapHocTheoMonHoc() {
        String tenMonHoc = (String) cbMonHoc.getSelectedItem();
        if (tenMonHoc != null) {
            MonHoc selectedMonHoc = monHocDAO.getAll().stream()
                .filter(m -> m.getTenMonHoc().equals(tenMonHoc))
                .findFirst().orElse(null);
            if (selectedMonHoc != null) {
                int monHocId = selectedMonHoc.getMonHocId();
                // Tìm capHocId từ boCauHoi (giả sử có bộ câu hỏi)
                List<BoCauHoi> boCauHoiList = boCauHoiDAO.getByMonHoc(monHocId);
                if (!boCauHoiList.isEmpty()) {
                    int capHocId = boCauHoiList.get(0).getCapHocId(); // Lấy capHocId từ bộ đầu tiên
                    CapHoc capHoc = capHocDAO.getAll().stream()
                        .filter(c -> c.getCapHocId() == capHocId)
                        .findFirst().orElse(null);
                    if (capHoc != null) {
                        cbCapHoc.setSelectedItem(capHoc.getTenCapHoc()); // Set cấp học
                    }
                }
            }
        }
    }

    private void loadCapHoc() {
        List<CapHoc> list = capHocDAO.getAll();
        cbCapHoc.removeAllItems();
        for (CapHoc c : list) {
            cbCapHoc.addItem(c.getTenCapHoc()); // Hiển thị tên
        }
    }

    private void loadMonHoc() {
        List<MonHoc> list = monHocDAO.getAll();
        cbMonHoc.removeAllItems();
        for (MonHoc m : list) {
            cbMonHoc.addItem(m.getTenMonHoc()); // Hiển thị tên
        }
    }

    private void loadBoCauHoi() {
        String tenCapHoc = (String) cbCapHoc.getSelectedItem();
        String tenMonHoc = (String) cbMonHoc.getSelectedItem();
        if (tenCapHoc != null && tenMonHoc != null) {
            int capHocId = capHocDAO.getAll().stream().filter(c -> c.getTenCapHoc().equals(tenCapHoc)).findFirst().get().getCapHocId();
            int monHocId = monHocDAO.getAll().stream().filter(m -> m.getTenMonHoc().equals(tenMonHoc)).findFirst().get().getMonHocId();
            List<BoCauHoi> list = boCauHoiDAO.getByMonHocAndCapHoc(monHocId, capHocId);
            if (!list.isEmpty()) {
                boCauHoiId = list.get(0).getBoCauHoiId();
                // Load tạm thời để lấy số lượng câu hỏi
                List<CauHoi> tempDanhSachCauHoi = cauHoiDAO.getByBoCauHoiId(boCauHoiId);
                maxSoCauHoi = tempDanhSachCauHoi.size();
                // Set spinner số câu hỏi về giá trị tối đa (tự nhảy)
                spSoCauHoi.setValue(Math.min((Integer) spSoCauHoi.getValue(), maxSoCauHoi));  // Giữ giá trị hiện tại nếu nhỏ hơn, hoặc set về max

                // Nếu checkbox được tích, tự động set về max
                if (chkMaxCauHoi.isSelected()) {
                    spSoCauHoi.setValue(maxSoCauHoi);
                }
            } else {
                maxSoCauHoi = 0;
                boCauHoiId = 0;
            }
        }
    }
    
    private void luuDapAnDangChon() {
        if (danhSachCauHoi == null || danhSachDapAn == null) return;

        int selectedIndex = -1;
        if (rbDapAn1.isSelected()) selectedIndex = 0;
        else if (rbDapAn2.isSelected()) selectedIndex = 1;
        else if (rbDapAn3.isSelected()) selectedIndex = 2;
        else if (rbDapAn4.isSelected()) selectedIndex = 3;

        if (selectedIndex != -1 && selectedIndex < danhSachDapAn.size()) {
            int cauHoiId = danhSachCauHoi.get(cauHienTai).getCauHoiId();
            int dapAnId = danhSachDapAn.get(selectedIndex).getDapAnId();
            dapAnDaChon.put(cauHoiId, dapAnId);
        }
    }
    


    private void hienThiCauHoi() {
        if (cauHienTai >= danhSachCauHoi.size()) {
        // Đến câu cuối, hiển thị xác nhận
        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hoàn thành bài thi?", "Xác nhận nộp bài", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            nopBai();
        } else {
            // Nếu không đồng ý, quay lại câu trước (hoặc giữ nguyên)
            cauHienTai--; // Quay lại câu cuối để tiếp tục
            hienThiCauHoi();
        }
        return;
        }      
        CauHoi ch = danhSachCauHoi.get(cauHienTai);
        taCauHoi.setText(ch.getNoiDung());
        // Cập nhật label câu hiện tại
        lblCauHienTai.setText("Câu " + (cauHienTai + 1) + "/" + danhSachCauHoi.size());

        // Kiểm tra nếu đã có thứ tự đáp án đã trộn cho câu này
        if (!thuTuDapAnDaTron.containsKey(ch.getCauHoiId())) {
            // Lần đầu: load và shuffle
            danhSachDapAn = dapAnDAO.getByCauHoiId(ch.getCauHoiId());
            Collections.shuffle(danhSachDapAn);
            thuTuDapAnDaTron.put(ch.getCauHoiId(), new ArrayList<>(danhSachDapAn)); // Lưu bản sao
        } else {
            // Quay lại: dùng lại thứ tự đã lưu
            danhSachDapAn = thuTuDapAnDaTron.get(ch.getCauHoiId());
        }

        bgDapAn.clearSelection();
        rbDapAn1.setText(danhSachDapAn.size() > 0 ? danhSachDapAn.get(0).getNoiDung() : "");
        rbDapAn2.setText(danhSachDapAn.size() > 1 ? danhSachDapAn.get(1).getNoiDung() : "");
        rbDapAn3.setText(danhSachDapAn.size() > 2 ? danhSachDapAn.get(2).getNoiDung() : "");
        rbDapAn4.setText(danhSachDapAn.size() > 3 ? danhSachDapAn.get(3).getNoiDung() : "");

        rbDapAn1.setVisible(danhSachDapAn.size() > 0);
        rbDapAn2.setVisible(danhSachDapAn.size() > 1);
        rbDapAn3.setVisible(danhSachDapAn.size() > 2);
        rbDapAn4.setVisible(danhSachDapAn.size() > 3);

        // Khôi phục đáp án đã chọn
        Integer dapAnIdDaChon = dapAnDaChon.get(ch.getCauHoiId());
        if (dapAnIdDaChon != null) {
            for (int i = 0; i < danhSachDapAn.size(); i++) {
                if (danhSachDapAn.get(i).getDapAnId() == dapAnIdDaChon) {
                    switch (i) {
                        case 0: rbDapAn1.setSelected(true); break;
                        case 1: rbDapAn2.setSelected(true); break;
                        case 2: rbDapAn3.setSelected(true); break;
                        case 3: rbDapAn4.setSelected(true); break;
                    }
                    break;
                }
            }
        }
    }

    private void nopBai() {
        if (timer != null) timer.cancel();
        int soCauDung = 0;
        for (Map.Entry<Integer, Integer> entry : dapAnDaChon.entrySet()) {
            DapAn da = dapAnDAO.getByCauHoiId(entry.getKey()).stream().filter(d -> d.getDapAnId() == entry.getValue()).findFirst().orElse(null);
            if (da != null && da.getDung() == 1) soCauDung++;
        }
        float diem = (float) soCauDung / danhSachCauHoi.size() * 10;

        BaiThi bt = new BaiThi();
        bt.setNguoiDungId(QuanLyPhien.layIdNguoiDungHienTai());
        bt.setBoCauHoiId(boCauHoiId);
        bt.setTongCau(danhSachCauHoi.size());
        bt.setSoCauDung(soCauDung);
        bt.setDiem(diem);
        bt.setThoiGianBatDau(thoiGianBatDau);
        bt.setThoiGianKetThuc(new Date(System.currentTimeMillis()));
        int baiThiId = baiThiDAO.insert(bt);

        for (Map.Entry<Integer, Integer> entry : dapAnDaChon.entrySet()) {
            ChiTietBaiThi ct = new ChiTietBaiThi();
            ct.setBaiThiId(baiThiId);
            ct.setCauHoiId(entry.getKey());
            ct.setDapAnDaChon(entry.getValue());
            DapAn da = dapAnDAO.getByCauHoiId(entry.getKey()).stream().filter(d -> d.getDapAnId() == entry.getValue()).findFirst().orElse(null);
            ct.setDung(da != null && da.getDung() == 1);
            baiThiDAO.insertChiTiet(ct);
        }

        JOptionPane.showMessageDialog(this, "Bài thi hoàn thành! Điểm: " + diem);
        
        new UserForm().setVisible(true);
        this.dispose();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnNopBai = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        btnCauTruoc = new javax.swing.JButton();
        btnTiepTheo = new javax.swing.JButton();
        btnBatDau = new javax.swing.JButton();
        btnTaiLai = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        taCauHoi = new javax.swing.JTextArea();
        spThoiGian = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        chkMaxCauHoi = new javax.swing.JCheckBox();
        spSoCauHoi = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        cbMonHoc = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cbCapHoc = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTimer = new javax.swing.JLabel();
        lblCauHienTai = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        rbDapAn1 = new javax.swing.JRadioButton();
        rbDapAn2 = new javax.swing.JRadioButton();
        rbDapAn3 = new javax.swing.JRadioButton();
        rbDapAn4 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnNopBai.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnNopBai.setText("Nộp bài");
        btnNopBai.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnNopBai.setContentAreaFilled(false);
        btnNopBai.addActionListener(this::btnNopBaiActionPerformed);

        btnThoat.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnThoat.setText("Về trang chủ");
        btnThoat.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnThoat.setContentAreaFilled(false);
        btnThoat.addActionListener(this::btnThoatActionPerformed);

        btnCauTruoc.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnCauTruoc.setText("Câu trước");
        btnCauTruoc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnCauTruoc.setContentAreaFilled(false);
        btnCauTruoc.addActionListener(this::btnCauTruocActionPerformed);

        btnTiepTheo.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnTiepTheo.setText("Câu tiếp theo");
        btnTiepTheo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnTiepTheo.setContentAreaFilled(false);
        btnTiepTheo.addActionListener(this::btnTiepTheoActionPerformed);

        btnBatDau.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnBatDau.setText("BẮT ĐẦU THI");
        btnBatDau.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnBatDau.setContentAreaFilled(false);
        btnBatDau.addActionListener(this::btnBatDauActionPerformed);

        btnTaiLai.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        btnTaiLai.setText("Tải Lại");
        btnTaiLai.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        btnTaiLai.setContentAreaFilled(false);
        btnTaiLai.addActionListener(this::btnTaiLaiActionPerformed);

        taCauHoi.setColumns(20);
        taCauHoi.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        taCauHoi.setRows(5);
        taCauHoi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jScrollPane2.setViewportView(taCauHoi);

        spThoiGian.setModel(new javax.swing.SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        spThoiGian.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        spThoiGian.addChangeListener(this::spThoiGianStateChanged);

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel5.setText("Thời Gian Làm Bài (phút):");

        chkMaxCauHoi.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        chkMaxCauHoi.setText("Max câu hỏi");

        spSoCauHoi.setModel(new javax.swing.SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        spSoCauHoi.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        spSoCauHoi.addChangeListener(this::spSoCauHoiStateChanged);

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel3.setText("Số Câu Hỏi:");

        cbMonHoc.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        cbMonHoc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        cbMonHoc.addActionListener(this::cbMonHocActionPerformed);

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel2.setText("Chọn Môn Học:");

        cbCapHoc.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        cbCapHoc.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel1.setText("Chọn Cấp Học:");

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel7.setText("Thời gian còn lại là:");

        lblTimer.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTimer.setForeground(new java.awt.Color(255, 0, 0));
        lblTimer.setText("thời gian");

        lblCauHienTai.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblCauHienTai.setText("Câu Hỏi");

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel4.setText("THI TRẮC NGHIỆM");

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel8.setText("B.");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel6.setText("A.");

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel9.setText("C.");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        jLabel10.setText("D.");

        rbDapAn1.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        rbDapAn1.setText("(Đáp án sẽ được set động)");
        rbDapAn1.addActionListener(this::rbDapAn1ActionPerformed);

        rbDapAn2.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        rbDapAn2.setText("(Đáp án sẽ được set động)");

        rbDapAn3.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        rbDapAn3.setText("(Đáp án sẽ được set động)");

        rbDapAn4.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        rbDapAn4.setText("(Đáp án sẽ được set động)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(620, 620, 620)
                            .addComponent(btnNopBai, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(cbCapHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbMonHoc, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spSoCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(chkMaxCauHoi))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnTaiLai, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(spThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(54, 54, 54)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 941, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(rbDapAn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(rbDapAn4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(rbDapAn2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(rbDapAn1, javax.swing.GroupLayout.PREFERRED_SIZE, 907, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(33, 33, 33)
                                    .addComponent(btnCauTruoc, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(btnTiepTheo, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTimer)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCauHienTai)
                                    .addGap(126, 126, 126))))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(lblTimer)
                    .addComponent(lblCauHienTai))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbCapHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbMonHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(spSoCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkMaxCauHoi))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTaiLai, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNopBai, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCauTruoc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTiepTheo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(rbDapAn1))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(rbDapAn2))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(rbDapAn3))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(rbDapAn4)
                    .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBatDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatDauActionPerformed
        // Code giống như batDauBaiThi()
        if (boCauHoiId == 0) {
            JOptionPane.showMessageDialog(this, "Chọn cấp học và môn học hợp lệ!");
            return;
        }
        
        // Chặn làm lại nếu bộ câu hỏi không công khai
        BoCauHoi bo = boCauHoiDAO.getById(boCauHoiId);
        if (bo != null && !bo.isCongKhai()) {
            int userId = QuanLyPhien.layIdNguoiDungHienTai();
            if (baiThiDAO.hasAttempt(userId, boCauHoiId)) {
                JOptionPane.showMessageDialog(this, "Bạn đã hoàn thành bài thi của bộ câu hỏi này!"
                        + " Mọi thắc mắc xin liên hệ người hướng dẫn!");
                return;
            }
        }
        
        int soCau = (Integer) spSoCauHoi.getValue();
        int thoiGian = (Integer) spThoiGian.getValue() * 60;
        thoiGianConLai = thoiGian;
        thoiGianBatDau = new Date(System.currentTimeMillis());

        danhSachCauHoi = cauHoiDAO.getByBoCauHoiId(boCauHoiId);
        Collections.shuffle(danhSachCauHoi);
        if (danhSachCauHoi.size() > soCau) danhSachCauHoi = danhSachCauHoi.subList(0, soCau);

        cauHienTai = 0;
        hienThiCauHoi();
        
        // Hiển thị 
        jLabel6.setVisible(true);
        jLabel7.setVisible(true);
        jLabel8.setVisible(true);
        jLabel9.setVisible(true);
        jLabel10.setVisible(true);
        lblTimer.setVisible(true);
        jScrollPane2.setVisible(true);
        rbDapAn1.setVisible(true);
        rbDapAn2.setVisible(true);
        rbDapAn3.setVisible(true);
        rbDapAn4.setVisible(true);
        btnCauTruoc.setVisible(true);
        btnTiepTheo.setVisible(true);
        btnNopBai.setVisible(true);
        lblCauHienTai.setVisible(true);
    
        // Ẩn
        btnThoat.setVisible(false);
        jLabel1.setVisible(false);
        cbCapHoc.setVisible(false);
        jLabel2.setVisible(false);
        cbMonHoc.setVisible(false);
        jLabel3.setVisible(false);
        spSoCauHoi.setVisible(false);
        jLabel5.setVisible(false);
        spThoiGian.setVisible(false);
        btnBatDau.setVisible(false);
        chkMaxCauHoi.setVisible(false);
        btnTaiLai.setVisible(false);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                thoiGianConLai--;
                lblTimer.setText(thoiGianConLai / 60 + ":" + String.format("%02d", thoiGianConLai % 60));
                if (thoiGianConLai <= 0) {
                    nopBai();
                }
            }
        }, 0, 1000);
    }//GEN-LAST:event_btnBatDauActionPerformed

    private void btnTiepTheoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTiepTheoActionPerformed
        cauHienTai++;
        hienThiCauHoi();
    }//GEN-LAST:event_btnTiepTheoActionPerformed

    private void btnNopBaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNopBaiActionPerformed
        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hoàn thành bài thi?", "Xác nhận nộp bài", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            nopBai();
        }
    }//GEN-LAST:event_btnNopBaiActionPerformed

    private void btnCauTruocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCauTruocActionPerformed
        if (cauHienTai > 0) {
        // Quay lại câu trước
        cauHienTai--;
        hienThiCauHoi();
    } else {
        JOptionPane.showMessageDialog(this, "Đây là câu đầu tiên!");
    }
    }//GEN-LAST:event_btnCauTruocActionPerformed

    private void cbMonHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMonHocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMonHocActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        new UserForm().setVisible(true);
        setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnTaiLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiLaiActionPerformed
        // Tải lại cấp học
        loadCapHoc();

        // Tải lại môn học (tất cả, hoặc lọc nếu cần)
        loadMonHoc();

        // Reset boCauHoiId để tránh trạng thái cũ
        boCauHoiId = 0;

        // Nếu muốn, tự động load môn học theo cấp học đầu tiên (nếu có)
        if (cbCapHoc.getItemCount() > 0) {
            cbCapHoc.setSelectedIndex(0);  // Chọn cấp học đầu tiên
            loadMonHocTheoCapHoc();        // Load môn học theo cấp học đã chọn
        } else {
            cbMonHoc.removeAllItems();     // Nếu không có cấp học, xóa môn học
        }

        // Reset các spinner nếu cần (ví dụ: số câu hỏi và thời gian về mặc định)
        spSoCauHoi.setValue(1);  // Giả sử mặc định 10 câu
        spThoiGian.setValue(30);  // Giả sử mặc định 30 phút
    }//GEN-LAST:event_btnTaiLaiActionPerformed

    private void spSoCauHoiStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spSoCauHoiStateChanged
        int currentValue = (Integer) spSoCauHoi.getValue();

        if (maxSoCauHoi > 0 && currentValue > maxSoCauHoi) {
            JOptionPane.showMessageDialog(
                this,
                "Bộ câu hỏi của môn học có " + maxSoCauHoi +
                " câu. Vui lòng chọn số câu hỏi không vượt quá giới hạn này."
            );
            spSoCauHoi.setValue(maxSoCauHoi);
        } else if (maxSoCauHoi == 0) {
            // Nếu chưa có bộ câu hỏi, reset về 1 và cảnh báo
            JOptionPane.showMessageDialog(this, "Chưa có bộ câu hỏi cho môn học này. Vui lòng chọn môn học khác.");
            spSoCauHoi.setValue(1);
        }
    }//GEN-LAST:event_spSoCauHoiStateChanged

    private void spThoiGianStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spThoiGianStateChanged
        int currentValue = (Integer) spThoiGian.getValue();

        if (currentValue >= 60) {
            JOptionPane.showMessageDialog(
                this,
                "Thời gian làm bài tối đa là 60 phút. Vui lòng chọn thời gian không vượt quá giới hạn này."
            );
            spThoiGian.setValue(60);
        }
    }//GEN-LAST:event_spThoiGianStateChanged

    private void rbDapAn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDapAn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbDapAn1ActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmLamBaiThi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatDau;
    private javax.swing.JButton btnCauTruoc;
    private javax.swing.JButton btnNopBai;
    private javax.swing.JButton btnTaiLai;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnTiepTheo;
    private javax.swing.JComboBox<String> cbCapHoc;
    private javax.swing.JComboBox<String> cbMonHoc;
    private javax.swing.JCheckBox chkMaxCauHoi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCauHienTai;
    private javax.swing.JLabel lblTimer;
    private javax.swing.JRadioButton rbDapAn1;
    private javax.swing.JRadioButton rbDapAn2;
    private javax.swing.JRadioButton rbDapAn3;
    private javax.swing.JRadioButton rbDapAn4;
    private javax.swing.JSpinner spSoCauHoi;
    private javax.swing.JSpinner spThoiGian;
    private javax.swing.JTextArea taCauHoi;
    // End of variables declaration//GEN-END:variables
}
