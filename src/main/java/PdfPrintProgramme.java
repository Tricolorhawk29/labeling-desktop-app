import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PdfPrintProgramme implements ActionListener {
    JFrame frame = new JFrame();
    JButton goBack = new JButton("back");
    JButton submit = new JButton("submit");

    JTextField inputSMON = new JTextField();
    JTextField inputCRM = new JTextField();
    JTextField inputClient = new JTextField();
    JTextField inputCountryDestination = new JTextField();
    JTextField inputContract = new JTextField();
    JTextField inputLocation = new JTextField();
    JTextField inputLabel2 = new JTextField();
    JTextField inputTransportType = new JTextField();

    public PdfPrintProgramme() {
        JLabel label = new JLabel("Wydrukuj etykiete");
        ImageIcon dwdIcon = new ImageIcon("dwd-logo.png");

        JPanel panel = new JPanel(new GridLayout(11, 2, 10, 10));
        panel.setBounds(100, 100, 800, 500);

        panel.add(new JLabel("SMON")); panel.add(inputSMON);
        panel.add(new JLabel("CRM")); panel.add(inputCRM);
        panel.add(new JLabel("Klient")); panel.add(inputClient);
        panel.add(new JLabel("Kraj przeznaczenia")); panel.add(inputCountryDestination);
        panel.add(new JLabel("Kontrakt")); panel.add(inputContract);
        panel.add(new JLabel("Obiekt")); panel.add(inputLocation);

        panel.add(new JLabel("Materiał"));
        JComboBox<String> inputMaterial = new JComboBox<>(new String[]{"PP", "HDPE"});
        panel.add(inputMaterial);

        panel.add(new JLabel("Typ dokumentu"));
        JComboBox<String> inputDocType = new JComboBox<>(new String[]{"WZ", "MM", "RW"});
        panel.add(inputDocType);

        panel.add(new JLabel("Etykieta1"));
        JComboBox<String> inputLabel1 = new JComboBox<>(new String[]{
                "Wpust Zeliwny", "Wpust Stalowy", "Saczek Poliamidowy",
                "Saczek Stalowy", "Rury PP", "Rury HD-PE", "Rury GRP", "Deski GRP"
        });
        panel.add(inputLabel1);

        panel.add(new JLabel("Etykieta2")); panel.add(inputLabel2);
        panel.add(new JLabel("Typ transportu")); panel.add(inputTransportType);

        frame.setTitle("Wydrukuj PDF");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setLayout(null);
        frame.setIconImage(dwdIcon.getImage());

        label.setFont(new Font("Arial", Font.BOLD, 40));
        label.setBounds(300, 0, 900, 80);

        goBack.setFont(new Font("Arial", Font.BOLD, 15));
        goBack.setBackground(Color.LIGHT_GRAY);
        goBack.setBounds(0, 0, 80, 50);
        goBack.setFocusable(false);
        goBack.addActionListener(this);

        submit.setFont(new Font("Arial", Font.BOLD, 15));
        submit.setBackground(Color.LIGHT_GRAY);
        submit.setBounds(470, 620, 100, 40);
        submit.setFocusable(false);
        submit.addActionListener(this);

        frame.add(label);
        frame.add(goBack);
        frame.add(panel);
        frame.add(submit);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goBack) {
            frame.dispose();
            new MyFrame(); // Make sure MyFrame exists
        }

        if (e.getSource() == submit) {
            if (areFieldsFilled()) {
                PDFData data = new PDFData(
                        inputSMON.getText(),
                        inputCRM.getText(),
                        inputClient.getText(),
                        inputCountryDestination.getText(),
                        inputContract.getText(),
                        inputLocation.getText(),
                        inputLabel2.getText()
                );

                createCustomPDF.generate(data);

            } else {
                JOptionPane.showMessageDialog(frame, "Proszę wypełnić wszystkie wymagane pola!", "Błąd", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static class PDFData {
        public String smon, crm, client, country, contract, location, label2;

        public PDFData(String smon, String crm, String client, String country, String contract, String location, String label2) {
            this.smon = smon;
            this.crm = crm;
            this.client = client;
            this.country = country;
            this.contract = contract;
            this.location = location;
            this.label2 = label2;
        }
    }

    private boolean areFieldsFilled() {
        return !inputSMON.getText().trim().isEmpty() &&
                !inputCRM.getText().trim().isEmpty() &&
                !inputClient.getText().trim().isEmpty() &&
                !inputCountryDestination.getText().trim().isEmpty() &&
                !inputContract.getText().trim().isEmpty() &&
                !inputLocation.getText().trim().isEmpty() &&
                !inputLabel2.getText().trim().isEmpty();
    }
}