using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Security.Cryptography;
using System.Text;

namespace WebAPI.Models.Helpers
{
    public static class Helper
    {
        public static readonly string PASSWORD_PLACEHOLDER = "********";

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

        public static T DeepClone<T>(this T obj)
        {
            using (var ms = new MemoryStream())
            {
                var formatter = new BinaryFormatter();
                formatter.Serialize(ms, obj);
                ms.Position = 0;

                return (T)formatter.Deserialize(ms);
            }
        }
    }
}