package tripkit.kakaoservice.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import java.io.*;
import java.net.HttpURLConnection;
import org.springframework.web.bind.annotation.*;
import tripkit.kakaoservice.service.UserService;

import java.net.URL;

@RequestMapping(value = "/login/oauth", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequiredArgsConstructor
public class OauthController {

    //kauth.kakao.com/oauth/authorize?client_id=11d34b21b1bc851cacad71f51341a5f2&redirect_uri=http://13.125.57.161:8080/login/oauth/kakao&response_type=code
    //kauth.kakao.com/oauth/authorize?client_id=11d34b21b1bc851cacad71f51341a5f2&redirect_uri=http://localhost:8080/login/oauth/kakao&response_type=code

    private final UserService service;
    private final String REST_API_KEY ="11d34b21b1bc851cacad71f51341a5f2";
    //private final String REDIRECT_URL ="http://13.125.57.161:8080/login/oauth/kakao";
    private final String REDIRECT_URL ="http://localhost:8080/login/oauth/kakao";

    String authCode; //인가 코드

    String access_Token="";
    String refresh_Token ="";
    String reqURL = "https://kauth.kakao.com/oauth/token";

    /**
     * refresh token, access_token을 받기 위해 있어야 하는 카카오의 Authorized code(인가 코드)를 받아오는 메서드
     * @param code
     * @return
     */
    @GetMapping("/kakao")
    public String kakao(@RequestParam String code){
        //String response = "성공적으로 카카오 로그인 API 코드를 불러왔습니다.";
        System.out.println(code); //auth code가 출력됨
        this.authCode = code;
        reqKakaoTokens(code);
        return  "access_token: "+this.access_Token+"\n"+"refresh_token: "+this.refresh_Token;
    }
    @GetMapping("/kakao/userInfo")
    public String kakaoUserInfo() throws IOException {
        return service.createKaKaoUserService(access_Token);
    }

    public void reqKakaoTokens(String code){

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // reqURL에 보낼 요청(Request 만들기)
            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true); //post이므로 서버에서 출력 스트림을 사용 가능한 상태로 만듦

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+REST_API_KEY); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri="+REDIRECT_URL); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과가 200이면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("statuscode: "+responseCode);

            //이제 요청을 통해 얻은 JSON값을 파싱해서 access_token이랑 refresh_token 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line ="";
            String result ="";
            while((line=br.readLine())!=null) result+=line;
            System.out.println("tokens response: "+result);
            //tokens response: {"access_token":"bC7SumogU7bEcxLxzKCZ9jdtKie2uHfdZmzRKPvUCj102wAAAYWj1m5X","token_type":"bearer","refresh_token":"-hDE7Qaw7fPqzf2_cTr3BcLeqnmmg9TO00S-jn09Cj102wAAAYWj1m5V","expires_in":21599,"scope":"profile_image profile_nickname","refresh_token_expires_in":5183999}
            getTokens(result);
            br.close();
            bw.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }


    public void getTokens(String tokenStr){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(tokenStr);

        this.access_Token = element.getAsJsonObject().get("access_token").getAsString();
        this.refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

    }
}
