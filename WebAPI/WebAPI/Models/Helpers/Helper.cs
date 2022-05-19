using System.Security.Cryptography;
using System.Text;

namespace WebAPI.Models.Helpers
{
    public static class Helper
    {
        public static string Hash(string text)
        {
            Encoding enc = Encoding.UTF8;

            return Hash(enc.GetBytes(text));
        }

        public static string Hash(byte[] byteArray)
        {
            StringBuilder Sb = new StringBuilder();

            using (SHA256 hash = SHA256.Create())
            {
                byte[] result = hash.ComputeHash(byteArray);

                foreach (byte b in result)
                {
                    Sb.Append(b.ToString("x2"));
                }
            }
            return Sb.ToString();
        }
    }
}