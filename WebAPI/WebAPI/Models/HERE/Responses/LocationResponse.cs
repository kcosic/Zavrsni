using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.HERE
{
    public class LocationResponse
    {
        [JsonProperty("items")]
        public Location[] Items { get; set; }

    }
}