using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.ORM;

namespace WebAPI.Models.DTOs
{
    [JsonObject]
    public class UserDTO
    {
        public UserDTO()
        {
            Cars = new HashSet<CarDTO>();
            Locations = new HashSet<LocationDTO>();
            Requests = new HashSet<RequestDTO>();
            Reviews = new HashSet<ReviewDTO>();
        }

        [JsonProperty]
        public int Id { get; set; }
        [JsonProperty]
        public DateTime DateCreated { get; set; }
        [JsonProperty]
        public DateTime DateModified { get; set; }
        [JsonProperty]
        public DateTime? DateDeleted { get; set; }
        [JsonProperty]
        public bool Deleted { get; set; }
        [JsonProperty]
        public string Username { get; set; }
        [JsonProperty]
        public string Password { get; set; }
        [JsonProperty]
        public string Email { get; set; }
        [JsonProperty]
        public string FirstName { get; set; }
        [JsonProperty]
        public string LastName { get; set; }
        [JsonProperty]
        public DateTime DateOfBirth { get; set; }
        [JsonProperty]
        public ICollection<CarDTO> Cars { get; set; }
        [JsonProperty]
        public ICollection<LocationDTO> Locations { get; set; }
        [JsonProperty]
        public ICollection<RequestDTO> Requests { get; set; }
        [JsonProperty]
        public ICollection<ReviewDTO> Reviews { get; set; }
        [JsonProperty]
        public ICollection<TokenDTO> Tokens { get; set; }
    }
}