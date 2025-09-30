import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class DocumentationGenerate implements ActionListener {

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    JFrame frame = new JFrame();
    JButton goBack = new JButton("cofnij");
    JButton submit = new JButton("Generuj raport");

    JTextField inputContract = new JTextField();
    JTextField inputLocation = new JTextField();
    JTextField inputReceiver = new JTextField();

    JComboBox<String> contractorNames = new JComboBox<>(new String[]{"A.S", "R.N", "J.S", "P.S", "Ł.S"});
    JTextField inputSender = new JTextField() {
        @Override
        public String getText() {
            Object sel = contractorNames.getSelectedItem();
            return sel == null ? "" : sel.toString();
        }
    };

    Map<File, String> imagesWithDescriptions = new LinkedHashMap<>();

    public DocumentationGenerate() {
        ImageIcon dwdIcon = new ImageIcon(getClass().getResource("/dwd-logo.png"));

        JLabel label = new JLabel("Stwórz raport montażowy");
        label.setFont(new Font("Arial", Font.BOLD, 40));
        label.setBounds(300, 0, 900, 80);

        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10));
        panel.setBounds(100, 100, 800, 400);
        panel.add(new JLabel("Kontrakt"));
        panel.add(inputContract);
        panel.add(new JLabel("Obiekt"));
        panel.add(inputLocation);
        panel.add(new JLabel("Klient"));
        panel.add(inputReceiver);
        panel.add(new JLabel("Montażysta"));
        panel.add(contractorNames);

        // === Drag & Drop / Clickable Panel ===
        JPanel dropPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.DARK_GRAY);
                float[] dash = {5f, 5f};
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 10f, dash, 0f));
                g2.drawRect(5, 5, getWidth() - 10, getHeight() - 10);

                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String msg = "👉 Przeciągnij i upuść zdjęcia tutaj lub kliknij, aby dodać 👈";
                int textWidth = fm.stringWidth(msg);
                int textHeight = fm.getAscent();
                g2.drawString(msg, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 5);
            }
        };
        dropPanel.setBounds(170, 520, 600, 100);
        dropPanel.setBackground(new Color(240, 240, 240));
        dropPanel.setLayout(new BorderLayout());

        // Drag & drop handler
        dropPanel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {
                try {
                    @SuppressWarnings("unchecked")
                    java.util.List<File> droppedFiles = (java.util.List<File>)
                            support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    int addedCount = 0;
                    for (File file : droppedFiles) {
                        String name = file.getName().toLowerCase();
                        if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
                            String desc = JOptionPane.showInputDialog(frame,
                                    "Podaj opis dla: " + file.getName(),
                                    "Opis zdjęcia",
                                    JOptionPane.PLAIN_MESSAGE);
                            if (desc == null) desc = "";
                            imagesWithDescriptions.put(file, desc);
                            addedCount++;
                        }
                    }

                    if (addedCount > 0) {
                        JOptionPane.showMessageDialog(frame,
                                "Łącznie dodano " + imagesWithDescriptions.size() + " zdjęć",
                                "Zdjęcia dodane",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });

        // Click to open file chooser
        dropPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                        "Obrazy (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"));

                int option = chooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = chooser.getSelectedFiles();
                    int addedCount = 0;
                    for (File file : selectedFiles) {
                        String desc = JOptionPane.showInputDialog(frame,
                                "Podaj opis dla: " + file.getName(),
                                "Opis zdjęcia",
                                JOptionPane.PLAIN_MESSAGE);
                        if (desc == null) desc = "";
                        imagesWithDescriptions.put(file, desc);
                        addedCount++;
                    }

                    if (addedCount > 0) {
                        JOptionPane.showMessageDialog(frame,
                                "Łącznie dodano " + imagesWithDescriptions.size() + " zdjęć",
                                "Zdjęcia dodane",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        // === Frame setup ===
        frame.setTitle("Wydrukuj Dokument");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLayout(null);
        frame.setIconImage(dwdIcon.getImage());

        goBack.setFont(new Font("Arial", Font.BOLD, 15));
        goBack.setBackground(Color.LIGHT_GRAY);
        goBack.setBounds(0, 0, 80, 50);
        goBack.setFocusable(false);
        goBack.addActionListener(this);

        submit.setFont(new Font("Arial", Font.BOLD, 15));
        submit.setBackground(Color.LIGHT_GRAY);
        submit.setBounds(280, 640, 300, 40);
        submit.setFocusable(false);
        submit.addActionListener(this);

        frame.add(label);
        frame.add(goBack);
        frame.add(panel);
        frame.add(dropPanel);
        frame.add(submit);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBack) {
            frame.dispose();
            new MyFrame(); // ⚠️ Make sure MyFrame exists
        }

        if (e.getSource() == submit) {
            if (areFieldsFilled()) {
                try {
                    Map<File, String> tempImagesWithDesc = copyImagesToTemp(imagesWithDescriptions);

                    PDFData data = new PDFData(
                            inputContract.getText(),
                            inputLocation.getText(),
                            inputReceiver.getText(),
                            inputSender.getText(),
                            tempImagesWithDesc
                    );

                    createDocumentPDF.generate(data); // ⚠️ Must exist

                    // Optional cleanup
                    cleanupTempImages(tempImagesWithDesc);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "Błąd podczas przetwarzania zdjęć: " + ex.getMessage(),
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Proszę wypełnić wszystkie wymagane pola!",
                        "Błąd",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private boolean areFieldsFilled() {
        return !inputContract.getText().trim().isEmpty() &&
                !inputLocation.getText().trim().isEmpty() &&
                !inputReceiver.getText().trim().isEmpty();
    }

    private Map<File, String> copyImagesToTemp(Map<File, String> originalImages) throws Exception {
        Map<File, String> tempMap = new LinkedHashMap<>();
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "dwd_images_temp");
        if (!tempDir.exists()) tempDir.mkdirs();

        for (Map.Entry<File, String> entry : originalImages.entrySet()) {
            File orig = entry.getKey();
            File tempFile = new File(tempDir, orig.getName());
            Files.copy(orig.toPath(), tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            tempMap.put(tempFile, entry.getValue());
        }

        return tempMap;
    }

    private void cleanupTempImages(Map<File, String> tempImages) {
        for (File f : tempImages.keySet()) {
            f.delete();
        }
    }

    public static class PDFData {
        public String contract, location, receiver, sender;
        public Map<File, String> imagesWithDescriptions;

        public PDFData(String contract, String location, String receiver, String sender, Map<File, String> imagesWithDescriptions) {
            this.contract = contract;
            this.location = location;
            this.receiver = receiver;
            this.sender = sender;
            this.imagesWithDescriptions = imagesWithDescriptions;
        }
    }
}
