using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.HERE
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