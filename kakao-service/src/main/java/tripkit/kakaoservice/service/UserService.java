package tripkit.kakaoservice.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UserService {

    public String createKaKaoUserService(String accessToken) throws IOException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        // accessToken을 사용하여 사용자 정보 조회
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);


        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);
        /* {"id":2619527153,"connected_at":"2023-01-12T02:30:24Z",
            "properties":
                {"nickname":"신정아",
                 "profile_image":"http://k.kakaocdn.net/dn/dqP3YD/btrVK1DtFot/e0fb7DdPyVy0frNmZX4D80/img_640x640.jpg",
                  "thumbnail_image":"http://k.kakaocdn.net/dn/dqP3YD/btrVK1DtFot/e0fb7DdPyVy0frNmZX4D80/img_110x110.jpg"},
            "kakao_account":
                {"profile_nickname_needs_agreement":false,
                "profile_image_needs_agreement":false,
                "profile":
                  {"nickname":"신정아",
                  "thumbnail_image_url":"http://k.kakaocdn.net/dn/dqP3YD/btrVK1DtFot/e0fb7DdPyVy0frNmZX4D80/img_110x110.jpg",
                  "profile_image_url":"http://k.kakaocdn.net/dn/dqP3YD/btrVK1DtFot/e0fb7DdPyVy0frNmZX4D80/img_640x640.jpg",
                  "is_default_image":false}
              }
          }
        * */

        //Gson 라이브러리로 JSON파싱
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        Long id = element.getAsJsonObject().get("id").getAsLong();
        JsonElement properties = element.getAsJsonObject().get("properties");
        String nickname = properties.getAsJsonObject().get("nickname").getAsString();
        br.close();
        return "id: "+id+" nickname: "+nickname;
    }
}
