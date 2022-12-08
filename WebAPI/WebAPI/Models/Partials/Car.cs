using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Car
    {

        public static CarDTO ToDTO(Car item, int level)
        {
            if (item == null || level <= 0)
            {
                return null;
            }

            return new CarDTO
            {
                Manufacturer = item.Manufacturer,
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Model = item.Model,
                Odometer = item.Odometer,
                User =User.ToDTO(item.User, level - 1),
                Requests = Request.ToListDTO(item.Requests, level - 1),
                UserId = item.UserId,
                Year = item.Year,
                LicensePlate = item.LicensePlate

            };
        }

        public static ICollection<CarDTO> ToListDTO(ICollection<Car> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public CarDTO ToDTO(int level)
        {
            return level > 0 ? new CarDTO
            {
                Manufacturer = Manufacturer,
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Model = Model,
                Odometer = Odometer,
                User = User.ToDTO(User, level - 1),
                UserId = UserId,
                Year = Year,
                LicensePlate = LicensePlate


            } : null;
        }
    }
}