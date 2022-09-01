using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebAPI.Models.HERE
{
    public class Location
    {
        [JsonProperty("title")]
        public string Title { get; set; }

        [JsonProperty("id")]
        public string Id { get; set; }

        [JsonProperty("resultType")]
        public string ResultType { get; set; }

        [JsonProperty("houseNumberType")]
        public string HouseNumberType { get; set; }

        [JsonProperty("address")]
        public Address Address { get; set; }

        [JsonProperty("position")]
        public LatLong Position { get; set; }

        [JsonProperty("access")]
        public List<LatLong> Access { get; set; }

        [JsonProperty("distance")]
        public int Distance { get; set; }

        [JsonProperty("mapView")]
        public MapView MapView { get; set; }
    }
}