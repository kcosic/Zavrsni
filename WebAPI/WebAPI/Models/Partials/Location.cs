using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{

    partial class Location {
        public static LocationDTO ToDTO(Location item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new LocationDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Shops = singleLevel ? null : Shop.ToListDTO(item.Shops),
                User = singleLevel ? null : User.ToDTO(item.User),
                UserId = item.UserId,
                City = item.City,
                Country = item.Country,
                County = item.County,
                Latitude = item.Latitude,
                Longitude = item.Longitude,
                Street = item.Street,
                StreetNumber = item.StreetNumber,

            };
        }

        public static ICollection<LocationDTO> ToListDTO(ICollection<Location> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public LocationDTO ToDTO(bool singleLevel = true)
        {
            return new LocationDTO
            {

                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Shops = singleLevel ? null : Shop.ToListDTO(Shops),
                User = singleLevel ? null : User.ToDTO(User),
                UserId = UserId,
                City = City,
                Country= Country,
                County= County,
                Latitude= Latitude,
                Longitude= Longitude,
                Street= Street,
                StreetNumber= StreetNumber,

            };
        }
    }

}