package com.superjuni5.buyornot_rest.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data @AllArgsConstructor
@JsonIgnoreProperties(value={"password", "ssn"})
public class User {
    private Integer id;

    @Size(min=2, message = "Name은 두 글자 이상 입력해주세요.")
    private String name;

    @Past
    private Date joinDate;

//    @JsonIgnore
    private String password;
//    @JsonIgnore
    private String ssn;
}
