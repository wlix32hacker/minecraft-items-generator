package minecraft.mod;

import com.mageddo.ramspiderjava.InstanceId;
import com.mageddo.ramspiderjava.InstanceValue;

import lombok.Value;

@Value
public class Player {

  InstanceValue value;

  int xp;

  public static Player from(InstanceValue value, int xp){
    return new Player(value, xp);
  }

  public InstanceId id(){
    return this.value.getId();
  }
}
