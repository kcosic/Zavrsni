using Newtonsoft.Json;
using WebAPI.Services.Models.HERE;

namespace WebAPI.Services.Models.Responses.HERE
{
    public class LocationResponse
    {
        [JsonProperty("items")]
        public Location[] Items { get; set; }

    }
}