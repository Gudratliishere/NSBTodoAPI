package com.gudratli.nsbtodoapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "email_tokens")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class EmailToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_token_id")
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
    private Date expireTime;

    public static EmailToken builder ()
    {
        return new EmailToken();
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

    public EmailToken expireMinutes (Integer minutes)
    {
        this.setExpireTime(new Date(Calendar.getInstance().getTimeInMillis() + (1000 * 60 * minutes)));
        return this;
    }

    public EmailToken build ()
    {
        if (this.expireTime == null)
            this.expireMinutes(5);
        return this;
    }
}
