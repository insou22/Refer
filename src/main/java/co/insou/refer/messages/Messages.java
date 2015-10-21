package co.insou.refer.messages;

import co.insou.refer.Refer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Messages {

    private Refer plugin;
    private YamlConfiguration conf;

	public String guiTitleSelector;
    public String guiTitleRefer;
    public String guiTitleRequest;

    public Messages (Refer plugin) {
        this.plugin = plugin;
        init();
    }

    private void init ()  {
        File configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            try {
                File defaultFile = new File("messages.yml");
                if (defaultFile.exists()) {
                    Files.copy( defaultFile.toPath(), configFile.toPath() );
                } else {
                    System.out.println("default messages file " + defaultFile.toPath().toString() + " not found!");
                }

            } catch (IOException e) {
                System.out.println(e);
            }
//            copyFile(System.in, configFile);
        }
//        conf = YamlConfiguration.loadConfiguration(configFile);
        loadMessages();
    }

    private void copyFile (InputStream input, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1028];
            int len;
            while ((len = input.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMessages () {
        guiTitleSelector = conf.getString("guiTitle.selector");
    }




}
