using Newtonsoft.Json;

namespace WebAPI.Services.Models.HERE
{
    public class LatLong
    {
        [JsonProperty("lat")]
        public string Latitude { get; set; }

        [JsonProperty("lng")]
        public string Longitude { get; set; }

    }
}