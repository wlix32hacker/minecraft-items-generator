package minecraft.mod;

import lombok.Data;

@Data
public class GameVersion {

  private String id;
  private String name;
  private String releaseTarget;
  private Integer worldVersion;
  private Integer protocolVersion;
  private Integer packVersion;
  private String buildTime;
  private Boolean stable;

}
