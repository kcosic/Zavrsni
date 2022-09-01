using Newtonsoft.Json;
using System;
using System.IO;
using System.Net.Http;
using System.Runtime.Serialization.Formatters.Binary;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;

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

        /// <summary>
        /// Calculates distance between two points on the map
        /// </summary>
        /// <param name="location1"></param>
        /// <param name="location2"></param>
        /// <returns>Distance in meters</returns>
        public static double CalculateDistanceBetweenPoints(Location location1, Location location2)
        {
            return CalculateDistanceBetweenPoints(location1.ToDTO(), location2.ToDTO());
        }

        /// <summary>
        /// Calculates distance between two points on the map
        /// </summary>
        /// <param name="location1"></param>
        /// <param name="location2"></param>
        /// <returns>Distance in meters</returns>
        public static double CalculateDistanceBetweenPoints(LocationDTO location1, LocationDTO location2)
        {
            var lat1 = location1.Latitude.Value;
            var lon1 = location1.Longitude.Value;
            var lat2 = location2.Latitude.Value;
            var lon2 = location2.Longitude.Value;

            var earthRadius = 6371e3; // meters
            var φ1 = lat2 * Math.PI / 180; // φ, λ in radians
            var φ2 = lat2 * Math.PI / 180;
            var Δφ = (lat2 - lat1) * Math.PI / 180;
            var Δλ = (lon2 - lon1) * Math.PI / 180;

            var a = Math.Sin(Δφ / 2) * Math.Sin(Δφ / 2) +
                      Math.Cos(φ1) * Math.Cos(φ2) *
                      Math.Sin(Δλ / 2) * Math.Sin(Δλ / 2);
            var c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));

            var distance = earthRadius * c; // in metres

            return distance;
        }

        public static string FromBase64(string base64Address)
        {
            return Encoding.UTF8.GetString(Convert.FromBase64String(base64Address));
        }
    }
}