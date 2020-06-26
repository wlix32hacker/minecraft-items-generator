package minecraft.mod.clientinfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.Validate;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class ClientInfoService {

  private final ObjectMapper objectMapper;
  private final OkHttpClient okHttpClient;
  private final VersionInfoService versionInfoService;

  @Inject
  public ClientInfoService(
      ObjectMapper objectMapper,
      OkHttpClient okHttpClient,
      VersionInfoService versionInfoService
  ) {
    this.objectMapper = objectMapper;
    this.okHttpClient = okHttpClient;
    this.versionInfoService = versionInfoService;
  }

  @SneakyThrows
  public String findClientClassMappingsText() {
    try (final Response res = this.okHttpClient
        .newCall(
            new Request
                .Builder()
                .url(this.findClientClassMappingsUrl())
                .get()
                .build()
        )
        .execute()
    ) {
      final String clientClassMappingsContent = res
          .body()
          .string();
      Validate.isTrue(res.isSuccessful(), clientClassMappingsContent);
      return clientClassMappingsContent;
    }
  }

  String findClientClassMappingsUrl() {
    return this.loadClientInfo()
        .at("/downloads/client_mappings/url")
        .asText();
  }

  @SneakyThrows
  JsonNode loadClientInfo() {
    return this.objectMapper.readTree(
        Files.newBufferedReader(this.getClientInfoPath(this.getVersion()))
    );
  }

  GameVersion getVersion() {
    return this.versionInfoService.getGameVersion();
  }

  Path getClientInfoPath(GameVersion version) {
    final Path clientInfoPath = Paths.get(String.format(
        "%s/.minecraft/versions/%s/%s.json",
        System.getenv("APPDATA"),
        version.getName(),
        version.getName()
    ));
    Validate.isTrue(Files.exists(clientInfoPath));
    Validate.isTrue(Files.isRegularFile(clientInfoPath));
    return clientInfoPath;
  }
}
