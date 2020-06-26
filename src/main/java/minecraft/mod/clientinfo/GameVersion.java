package minecraft.mod.clientinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
