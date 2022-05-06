package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "email_tokens")
@Data
@NoArgsConstructor
public class EmailToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    @Column(name = "email")
    private String email;
    @NonNull
    @Column(name = "token")
    private String token;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "expire_time")
    @NonNull
    private Date expire_time;

    public static EmailToken builder ()
    {
        EmailToken emailToken = new EmailToken();
        return emailToken;
    }

    public EmailToken addEmail (String email)
    {
        this.setEmail(email);
        return this;
    }

    public EmailToken addToken (String token)
    {
        this.setToken(token);
        return this;
    }

    public EmailToken expireMinutes(Integer minutes)
    {
        this.setExpire_time(new Date(Calendar.getInstance().getTimeInMillis() + (1000 * 60 * minutes)));
        return this;
    }

    public EmailToken build ()
    {
        if (this.expire_time == null)
            this.expireMinutes(5);
        return this;
    }
}
