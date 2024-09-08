package com.transfer.entity.key;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class FavRecipientId implements Serializable {

    @Column(nullable = false)
    private Long customerID;

    @Column(nullable = false)
    private Long recipientAccountID;
}