package ntu.hung.coffeemystar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{
    // Phương thức hashMD5 để mã hóa chuỗi đầu vào theo thuật toán MD5
    public static String hashMD5(String input)
    {
        try
        {
            // Tạo đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Chuyển chuỗi đầu vào thành mảng byte và thực hiện hàm băm MD5
            byte[] messageDigest = md.digest(input.getBytes());

            // Dùng StringBuilder để xây dựng chuỗi kết quả dưới dạng hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest)
            {
                // Chuyển từng byte thành chuỗi hex và thêm vào StringBuilder
                hexString.append(String.format("%02x", b));
            }

            // Trả về chuỗi MD5 dưới dạng hex
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            // Xử lý lỗi nếu thuật toán MD5 không tồn tại
            e.printStackTrace();
            throw new RuntimeException("MD5 algorithm not found!"); // Ném ngoại lệ nếu không tìm thấy thuật toán MD5
        }
    }
}

