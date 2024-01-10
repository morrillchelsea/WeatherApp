import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp extends JFrame {

    private JTextField zipCodeField;
    private JTextArea weatherDataArea;

    public WeatherApp() {
        super("Weather App");

        zipCodeField = new JTextField(10);
        JButton getWeatherButton = new JButton("Get Weather");
        weatherDataArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(weatherDataArea);

        setLayout(new FlowLayout());

        add(new JLabel("Enter Zip Code: "));
        add(zipCodeField);
        add(getWeatherButton);
        add(scrollPane);

        getWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String zipCode = zipCodeField.getText();
                if (!zipCode.isEmpty()) {
                    String weatherData = getWeatherData(zipCode);
                    weatherDataArea.setText(weatherData);
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String getWeatherData(String zipCode) {
        try {
            URL url = new URL("https://api.weather.com/data/2.5/weather?zip=" + zipCode + "&appid=User-Agent: (myweatherapp.com, contact@myweatherapp.com)");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching weather data";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherApp();
            }
        });
    }
}
