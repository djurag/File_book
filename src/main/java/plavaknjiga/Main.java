package plavaknjiga;


import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField pathField;
    private Path path = Paths.get("data.csv");
    private final List<Record> records = new ArrayList<>();
    private JTable jTable;
    private JFileChooser fileChooser;
    private FinalTableModel model;
    private static final JLabel copyright = new JLabel("\u00a9 Darko Juraga, darko.juraga@outlook.com, April 2020.");

    private Main() {
        initializeWindow();
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel top = topPanel();
        contentPane.add(top, BorderLayout.NORTH);

        JScrollPane pane = centerPanel();
        contentPane.add(pane, BorderLayout.CENTER);

        JPanel bottom = bottomPanel();
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private JPanel bottomPanel() {
        JPanel bottomPanel = new JPanel();
        FlowLayout fl_bottom = (FlowLayout) bottomPanel.getLayout();
        fl_bottom.setAlignment(FlowLayout.CENTER);
        JButton btnExit = new JButton("Kraj rada");
        btnExit.addActionListener(e -> System.exit(0));
        bottomPanel.add(btnExit, BorderLayout.WEST);
        JButton delete = new JButton("Ukloni spis");
        bottomPanel.add(delete, BorderLayout.CENTER);
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("Traži");
        searchField.setColumns(20);
        bottomPanel.add(searchField);
        bottomPanel.add(searchBtn);

        //delete listener
        delete.addActionListener(e -> {
            DeleteClient deleteClient = new DeleteClient();
            if (!deleteClient.isVisible()) {
                deleteClient.setVisible(true);
            } else if (deleteClient.isVisible()) {
                deleteClient.setVisible(false);
            }
        });
        //search listener
        searchBtn.addActionListener(e -> {
            String search = searchField.getText();
            List<Record> records1;
            if (records.isEmpty()) {
                setWarningMessage("Zapisi za pretraživanje nisu učitani ili zapisi ne postoje u datoteci!\n" +
                        "Učitaj datoteku i pokušaj ponovo.");
            } else {
                if (StringUtils.isNumeric(search)) {
                    records1 = searchOnAct(Integer.parseInt(search));
                } else {
                    records1 = searchOnName(search);
                }
                if (records1.isEmpty()) {
                    setWarningMessage("Nema zapisa koji odgovaraju upitu");
                } else {
                    model = new FinalTableModel(records1);
                    jTable.setModel(model);
                }
            }
        });

        JPanel bottom = new JPanel(new GridLayout(2, 1));
        copyright.setHorizontalAlignment(JLabel.CENTER);
        bottom.add(bottomPanel, BorderLayout.CENTER);
        bottom.add(copyright, BorderLayout.CENTER);
        return bottom;
    }

    private JScrollPane centerPanel() {
        model = new FinalTableModel(records);
        jTable = new JTable(model);
        jTable.setAutoCreateRowSorter(true);
        return new JScrollPane(jTable);
    }

    private JPanel topPanel() {
        JPanel top = new JPanel();
        JButton btnLoadFile = new JButton("Učitaj datoteku");
        JButton newEntry = new JButton("Novi spis");
        FlowLayout fl_top = (FlowLayout) top.getLayout();
        fl_top.setAlignment(FlowLayout.LEFT);
        JButton openBtn = new JButton("Otvori");
        top.add(openBtn);
        pathField = new JTextField();
        pathField.setEditable(false);
        top.add(pathField);
        pathField.setColumns(15);
        File f = path.toFile();
        pathField.setText(f.getName());
        JButton refresBtn = new JButton("Ažuriraj tablicu");
        refresBtn.addActionListener(e -> {
            model = new FinalTableModel(records);
            jTable.setModel(model);
        });
        openBtn.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                path = Paths.get(file.toURI());
                pathField.setText(file.getName());
            }
        });
        //ucitaj datoteku listener
        btnLoadFile.addActionListener(e -> {
            Thread load = new Thread(() -> {
                try {
                    records.clear();
                    Files.lines(path).forEach(str -> records.add(getRecordFromLine(str)));
                    if (records.isEmpty()) {
                        setWarningMessage("Ne postoje zapisi u datoteci!");
                    } else {
                        model = new FinalTableModel(records);
                        jTable.setModel(model);
                    }
                } catch (IOException ignored) {
                }
            });
            load.start();
        });
        top.add(btnLoadFile);
        top.add(newEntry);
        top.add(refresBtn);
        //novi klijent listener
        newEntry.addActionListener(e -> {
            NewClient client = new NewClient();
            if (!client.isVisible()) {
                client.setVisible(true);
            } else if (client.isVisible()) {
                client.setVisible(false);
            }
        });
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                else
                    return f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "Coma Separated Values (*.csv)";
            }
        });
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        return top;
    }

    private void initializeWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(710, 400);
        this.setResizable(false);
        setLocationRelativeTo(null);
    }

    private List<Record> searchOnName(String search) {
        List<Record> result = new ArrayList<>();
        for (Record r :
                records) {
            if (r.getName().toLowerCase().contains(search.toLowerCase())) {
                result.add(r);
            }
        }
        return result;
    }

    private List<Record> searchOnAct(int search) {
        List<Record> result = new ArrayList<>();
        for (Record r :
                records) {
            if (r.getAct() == search) {
                result.add(r);
            }
        }
        return result;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                Main app = new Main();
                app.setTitle("Plava Knjiga");
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private static Record getRecordFromLine(String nextLine) {
        String name;
        int act;
        String[] line = nextLine.split(",");
        name = line[0];
        act = Integer.parseInt(line[1]);
        return new Record(name, act);
    }

    class NewClient extends JFrame {
        NewClient() {
            initializeNewClient();
            JPanel container = new JPanel();
            container.setBorder(new EmptyBorder(5, 5, 5, 5));
            container.setLayout(new BorderLayout(0, 0));
            setContentPane(container);

            //ime i prezime
            JPanel top = new JPanel();
            JLabel nameLabel = new JLabel("Ime:");
            JTextField nameText = new JTextField();
            nameText.setColumns(20);
            FlowLayout fl_top = (FlowLayout) top.getLayout();
            fl_top.setAlignment(FlowLayout.LEFT);
            top.add(nameLabel, BorderLayout.CENTER);
            top.add(nameText, BorderLayout.CENTER);
            container.add(top, BorderLayout.NORTH);
            //spis
            JPanel center = new JPanel();
            JLabel actLabel = new JLabel("Broj spisa:");
            JTextField actText = new JTextField();
            actText.setColumns(17);
            center.add(actLabel, BorderLayout.EAST);
            FlowLayout fl_cent = (FlowLayout) center.getLayout();
            fl_cent.setAlignment(FlowLayout.LEFT);
            center.add(actText, BorderLayout.CENTER);
            container.add(center, BorderLayout.CENTER);
            //gumbi
            JButton save = new JButton("Spremi");
            JButton cancel = new JButton("Odbaci");
            JPanel bottom = new JPanel();
            bottom.add(cancel, BorderLayout.EAST);
            bottom.add(save, BorderLayout.WEST);
            container.add(bottom, BorderLayout.SOUTH);
            cancel.addActionListener(e -> setVisible(false));

            save.addActionListener(e -> {
                String name = nameText.getText().trim();
                if (!StringUtils.isNumeric(actText.getText()))
                    setWarningMessage("Broj spisa mora sadržavati samo brojeve!");
                else {
                    int act = Integer.parseInt(actText.getText().trim());
                    Record newRecord = new Record(name, act);
                    String[] streamRecord = {name, String.valueOf(act)};
                    if (!records.contains(newRecord)) {
                        if (!containAct(newRecord)) {
                            addToFile(streamRecord, newRecord);
                            model = new FinalTableModel(records);
                            jTable.setModel(model);
                            JOptionPane.showMessageDialog(this, "Zapis " + newRecord.toString() + " dodan je u tablicu.",
                                    "Zapis uspješno dodan", JOptionPane.PLAIN_MESSAGE);
                        } else
                            setWarningMessage("Broj spisa već postoji!");
                    } else {
                        setWarningMessage("Postoji spis sa istim imenom i brojem spisa!");
                    }
                }
            });
        }

        private void initializeNewClient() {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 150);
            setLocationRelativeTo(Main.this);
            this.setResizable(false);
            setTitle("Novi spis");
        }
    }

    class DeleteClient extends JFrame {
        DeleteClient() {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(350, 100);
            this.setResizable(false);
            setLocationRelativeTo(Main.this);
            setTitle("Ukloni spis");
            JPanel container = new JPanel();
            container.setBorder(new EmptyBorder(5, 5, 5, 5));
            container.setLayout(new BorderLayout(0, 0));
            setContentPane(container);
            //spis
            JPanel center = new JPanel();
            JLabel actLabel = new JLabel("Broj spisa:");
            JTextField actText = new JTextField();
            actText.setColumns(20);
            center.add(actLabel, BorderLayout.EAST);
            FlowLayout fl_cent = (FlowLayout) center.getLayout();
            fl_cent.setAlignment(FlowLayout.CENTER);
            center.add(actText, BorderLayout.CENTER);
            container.add(center, BorderLayout.CENTER);
            //gumbi
            JButton save = new JButton("Ukloni");
            JButton cancel = new JButton("Odbaci");
            JPanel bottom = new JPanel();
            bottom.add(cancel, BorderLayout.EAST);
            bottom.add(save, BorderLayout.WEST);
            container.add(bottom, BorderLayout.SOUTH);
            cancel.addActionListener(e -> setVisible(false));

            save.addActionListener(e -> {
                Record deleteRecord;
                if (actText.getText().trim().isEmpty()) {
                    setWarningMessage("Broj spisa mora biti unesen!");
                } else if (!StringUtils.isNumeric(actText.getText().trim()))
                    setWarningMessage("Broj spisa mora sadržavati samo brojeve!");
                else {
                    deleteRecord = getFromRecordsAsAct(Integer.parseInt(actText.getText().trim()));

                    int reply = JOptionPane.showConfirmDialog(this, "Jeste li sigurni da želite ukloniti zapis?");
                    if (reply == JOptionPane.YES_OPTION) {
                        this.setVisible(false);
                        if (deleteRecord == null || !records.contains(deleteRecord)) {
                            setWarningMessage("Nije moguće ukloniti zapis!\nZapis sa navedenim brojem spisa ne postoji!");
                        } else {
                            records.remove(deleteRecord);
                            updateFile();
                            model = new FinalTableModel(records);
                            jTable.setModel(model);
                            JOptionPane.showMessageDialog(this, "Zapis " + deleteRecord.toString() + " uklonjen je iz tablice.",
                                    "Spis uspješno uklonjen", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            });

        }
    }

    private boolean containAct(Record newRecord) {
        for (Record r :
                records) {
            if (r.getAct() == newRecord.getAct())
                return true;
        }
        return false;
    }

    private void addToFile(String[] streamRecord, Record newRecord) {
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(path.toFile(), true), CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            csvWriter.writeNext(streamRecord);
            csvWriter.close();
            records.add(newRecord);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void setWarningMessage(String s) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane optionPane = new JOptionPane(s, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = optionPane.createDialog("Upozorenje!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    private Record getFromRecordsAsAct(int text) {
        for (Record r :
                records) {
            if (r.getAct() == text)
                return r;
        }
        return null;
    }

    private void updateFile() {
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(path.toFile()), CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            for (Record r :
                    records) {
                String[] stringRecord = {r.getName(), String.valueOf(r.getAct())};
                csvWriter.writeNext(stringRecord);
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
