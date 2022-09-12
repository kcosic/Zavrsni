using Newtonsoft.Json;

namespace WebAPI.Services.Models.HERE
{
    public class MapView
    {

        [JsonProperty("west")]
        public double West { get; set; }

        [JsonProperty("south")]
        public double South { get; set; }

        [JsonProperty("east")]
        public double East { get; set; }

        [JsonProperty("north")]
        public double North { get; set; }
    }
}