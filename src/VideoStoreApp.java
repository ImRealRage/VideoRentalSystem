import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VideoStoreApp extends JFrame implements ActionListener {

    private JTextField videoNameField;
    private JTextArea outputArea;
    private JComboBox<String> videoList;
    private JButton addVideoButton;
    private JButton checkoutButton;
    private JButton returnButton;
    private JButton rateButton;
    private JButton listInventoryButton;
    private JButton exitButton;

    private Video[] store;

    public VideoStoreApp() {
        super("Video Store by Kanishk Shukla");

        store = new Video[5];

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Video Store");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        videoNameField = new JTextField(20);
        addVideoButton = new JButton("Add Video");
        addVideoButton.addActionListener(this);
        videoList = new JComboBox<>();
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Video Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(videoNameField, gbc);
        gbc.gridx = 2;
        formPanel.add(addVideoButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Select Video:"), gbc);
        gbc.gridx = 1;
        formPanel.add(videoList, gbc);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        outputArea = new JTextArea(10, 20);
        outputArea.setEditable(false);
        centerPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        checkoutButton = createButton("Check Out", this);
        returnButton = createButton("Return", this);
        rateButton = createButton("Rate", this);
        listInventoryButton = createButton("List Inventory", this);
        exitButton = createButton("Exit", this);
        bottomPanel.add(checkoutButton);
        bottomPanel.add(returnButton);
        bottomPanel.add(rateButton);
        bottomPanel.add(listInventoryButton);
        bottomPanel.add(exitButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addVideoButton) {
            String videoName = videoNameField.getText();
            addVideo(videoName);
            updateVideoList();
        } else if (e.getSource() == checkoutButton) {
            String videoName = videoList.getSelectedItem().toString();
            doCheckout(videoName);
            updateOutput();
        } else if (e.getSource() == returnButton) {
            String videoName = videoList.getSelectedItem().toString();
            doReturn(videoName);
            updateOutput();
        } else if (e.getSource() == rateButton) {
            String videoName = videoList.getSelectedItem().toString();
            int rating = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Rating for " + videoName + ":"));
            receiveRating(videoName, rating);
            updateOutput();
        } else if (e.getSource() == listInventoryButton) {
            updateOutput();
        } else if (e.getSource() == exitButton) {
            JOptionPane.showMessageDialog(this, "Thanks for using the application!");
            System.exit(0);
        }
    }

    private void addVideo(String name) {
        for (int i = 0; i < store.length; i++) {
            if (store[i] == null) {
                store[i] = new Video(name);
                JOptionPane.showMessageDialog(this, "Video \"" + name + "\" added successfully");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Video store is full. Cannot add more videos.");
    }

    private void doCheckout(String name) {
        for (Video video : store) {
            if (video != null && video.getName().equals(name)) {
                video.doCheckout();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Video \"" + name + "\" not found in inventory.");
    }

    private void doReturn(String name) {
        for (Video video : store) {
            if (video != null && video.getName().equals(name)) {
                video.doReturn();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Video \"" + name + "\" not found in inventory.");
    }

    private void receiveRating(String name, int rating) {
        for (Video video : store) {
            if (video != null && video.getName().equals(name)) {
                video.receiveRating(rating);
                JOptionPane.showMessageDialog(this,
                        "Rating \"" + rating + "\" has been mapped to the Video \"" + name + "\"");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Video \"" + name + "\" not found in inventory.");
    }

    private void updateVideoList() {
        videoList.removeAllItems();
        for (Video video : store) {
            if (video != null) {
                videoList.addItem(video.getName());
            }
        }
    }

    private void updateOutput() {
        StringBuilder inventory = new StringBuilder("Video Name          | Checkout Status | Rating\n");
        inventory.append("---------------------------------------------\n");
        for (Video video : store) {
            if (video != null) {
                String name = video.getName();
                String checkoutStatus = video.getCheckout() ? "Checked Out" : "Available";
                String rating = String.valueOf(video.getRating());
                // Adjust column widths
                inventory.append(String.format("%-20s | %-15s | %-6s%n", name, checkoutStatus, rating));
            }
        }
        inventory.append("---------------------------------------------");
        outputArea.setText(inventory.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VideoStoreApp());
    }
}

class Video {

    private String videoName;
    private boolean checkout;
    private int rating;

    public Video(String name) {
        this.videoName = name;
    }

    public String getName() {
        return videoName;
    }

    public void doCheckout() {
        checkout = true;
    }

    public void doReturn() {
        checkout = false;
    }

    public void receiveRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public boolean getCheckout() {
        return checkout;
    }
}
