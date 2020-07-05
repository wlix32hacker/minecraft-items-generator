package minecraft.mod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mageddo.ramspiderjava.InstanceId;
import com.mageddo.ramspiderjava.InstanceValue;

import org.apache.commons.lang3.Validate;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "mapName")
public class Player {

  InstanceValue value;
  String mapName;
  int xp;
  int version;

  public static Player from(InstanceValue value, int xp){
    return new Player(
        value, findName(value.getValue()),
        xp,
        findVersion(value.getValue())
    );
  }

  static int findVersion(String value) {
    final Matcher matcher = Pattern
        .compile("'[^']+'/(\\d+)")
        .matcher(value);
    Validate.isTrue( matcher.find(), "Couldn't find map name: %s", value);
    return Integer.parseInt(matcher.group(1));
  }

  static String findName(String value) {
    final Matcher matcher = Pattern
        .compile("ServerLevel\\[([^\\]]+)\\]")
        .matcher(value);
    Validate.isTrue( matcher.find(), "Couldn't find map name: %s", value);
    return matcher.group(1);
  }

  public InstanceId id(){
    return this.value.getId();
  }
}
