package upb.tvtrack;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<UserEntity> getAll();

    @Query("SELECT * FROM user where user_email LIKE :in_email")
    UserEntity findByEmail(byte[] in_email);

    @Query("SELECT COUNT(*) from user")
    int countUsers();

    @Query("SELECT COUNT(*) from user where user_email like :in_email and user_pass like :in_pass")
    int existsUser(byte[] in_email, byte[] in_pass);

    @Insert
    void insertAll(UserEntity... users);

    @Delete
    void delete(UserEntity user);
}
