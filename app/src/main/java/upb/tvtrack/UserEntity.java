package upb.tvtrack;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "user_email")
    private byte[] email_hash;

    @ColumnInfo(name = "user_pass")
    private byte[] password_hash;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte[] getEmail_hash() {
        return this.email_hash;
    }

    public void setEmail_hash(byte[] new_email_hash) {
        this.email_hash = new_email_hash;
    }

    public byte[] getPassword_hash() {
        return this.password_hash;
    }

    public void setPassword_hash(byte[] new_pass_hash) {
        this.password_hash = new_pass_hash;
    }
}
