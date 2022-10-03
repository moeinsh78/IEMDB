package com.ie.CA8.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ie.CA8.Errors.UserAlreadyExistsError;
import com.ie.CA8.Errors.UserNotFoundError;
import com.ie.CA8.Service.IEMDBSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import static javax.xml.bind.DatatypeConverter.parseDateTime;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000",exposedHeaders = "*")
@RestController
public class LoginController {
    @Autowired
    private IEMDBSystem iemdbSystem;

    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    protected ResponseEntity<Void> signup(@RequestBody Map<String, String> user_info){
        String name = user_info.get("name");
        String userEmail = user_info.get("email");
        String password = user_info.get("password");
        String nickname = user_info.get("nickname");
        String birth_date = user_info.get("birthdate");
        try {
            iemdbSystem.signup(name, userEmail, password, nickname, birth_date);
        } catch (UserAlreadyExistsError e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String jwt_token = iemdbSystem.login(userEmail, password);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwt_token);
            map.add("userEmail",user_info.get("email"));
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (UserNotFoundError ignored) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    protected ResponseEntity<Void> login(@RequestBody Map<String, String> user_info){
        try {
            String jwt_token = iemdbSystem.login(user_info.get("email"), user_info.get("password"));
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwt_token);
            map.add("userEmail",user_info.get("email"));
            return new ResponseEntity<>(map,HttpStatus.OK);
        } catch (UserNotFoundError ignored) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/callback",method = RequestMethod.GET)
    protected ResponseEntity<Void> callbackLogin(@RequestParam(value = "code", required = true) String code) throws IOException, InterruptedException, ParseException {
        if(iemdbSystem.getLoggedInUser() != null) {
            String jwt_token = iemdbSystem.createJwtToken(iemdbSystem.getLoggedInUser().getEmail());
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", jwt_token);
            map.add("userEmail",iemdbSystem.getLoggedInUser().getEmail());
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        String client_id = "4e1a1049c6dea3c2480a";
        String client_secret = "f69a0287390d805292b630f778e102aa4192fa64";
        String access_token_url = String.format(
                "https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s",
                client_id, client_secret, code
        );
        HttpClient client = HttpClient.newHttpClient();
        URI access_token_uri = URI.create(access_token_url);
        HttpRequest.Builder access_token_builder = HttpRequest.newBuilder().uri(access_token_uri);
        HttpRequest access_token_request = access_token_builder
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> access_token_result = client.send(access_token_request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> result_body = mapper.readValue(access_token_result.body(),HashMap.class);
        String access_token = (String) result_body.get("access_token");
        URI user_data_uri = URI.create("https://api.github.com/user");
        HttpRequest.Builder user_data_builder = HttpRequest.newBuilder().uri(user_data_uri);
        HttpRequest request = user_data_builder.GET()
                .header("Authorization", String.format("token %s",access_token))
                .build();
        HttpResponse<String> user_data_result = client.send(request,HttpResponse.BodyHandlers.ofString());
        HashMap data_body = mapper.readValue(user_data_result.body(),HashMap.class);
        String user_email = (String) data_body.get("email");
        String nickname = (String) data_body.get("login");
        String name = (String) data_body.get("name");
        String bday = (String) data_body.get("created_at");
        Calendar cal = parseDateTime(bday);
        cal.add(Calendar.YEAR, -18);
        Date date = cal.getTime();
        String birthdate = new SimpleDateFormat("yyyy/MM/dd").format(date).toString();
        String password = "null";
        try {
            iemdbSystem.signup(name, user_email, password, nickname, birthdate);
        } catch (UserAlreadyExistsError e) {
            iemdbSystem.updateUser(name,user_email,password,nickname,birthdate);
        }
        iemdbSystem.loginWithJwtToken(user_email);
        String jwt_token = iemdbSystem.createJwtToken(user_email);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", jwt_token);
        map.add("userEmail",user_email);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
}
