package fr.genielogiciel.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.genielogiciel.model.entity.User;
import fr.genielogiciel.model.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GeneralService {
    @Autowired
    private UserRepository userRepository;

    public User getUserFromContext() {
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByMail(mail).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Mail %s not found", mail))
        );
    }

    public <T> List<T> getObjectListFromJsonString(String json, Class<T> tClass) throws JSONException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JSONArray jsonArray = new JSONArray(json);
        List<T> objectList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++) {
            objectList.add(mapper.readValue(jsonArray.get(i).toString(), tClass));
        }
        return objectList;
    }

}
