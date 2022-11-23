using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{

    partial class Location {
        public static LocationDTO ToDTO(Location item, int level)
        {
            if (item == null || level <= 0)
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
                Shops = Shop.ToListDTO(item.Shops, level - 1),
                User = User.ToDTO(item.User, level - 1),
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

        public static ICollection<LocationDTO> ToListDTO(ICollection<Location> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public LocationDTO ToDTO(int level)
        {
            return level > 0 ? new LocationDTO
            {

                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Shops = Shop.ToListDTO(Shops, level),
                User = User.ToDTO(User, level),
                UserId = UserId,
                City = City,
                Country= Country,
                County= County,
                Latitude= Latitude,
                Longitude= Longitude,
                Street= Street,
                StreetNumber= StreetNumber,

            } : null;
        }
    }

}