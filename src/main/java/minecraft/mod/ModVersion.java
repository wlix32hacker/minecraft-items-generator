package minecraft.mod;

import java.util.Optional;
import java.util.Properties;

import lombok.SneakyThrows;

public class ModVersion {

  public static String getVersion(){
    final Properties props = loadProps();
    return Optional.ofNullable(props.getProperty("version")).orElse("Unknown Version");
  }

  @SneakyThrows
  private static Properties loadProps() {
    final Properties props = new Properties();
    props.load(ModVersion.class.getResourceAsStream("/application.properties"));
    return props;
  }
}
