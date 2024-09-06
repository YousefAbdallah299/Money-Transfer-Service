package com.transfer.repository;

import com.transfer.entity.FavRecipient;
import com.transfer.entity.key.FavRecipientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRecipientRepository extends JpaRepository<FavRecipient, FavRecipientId> {
}
