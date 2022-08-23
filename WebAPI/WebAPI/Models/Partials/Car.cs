using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Car {
    
        public static CarDTO ToDTO(Car item, bool singleLevel = true)
        {
            return new CarDTO
            {
                Make = item.Make,
                Manufacturer = item.Manufacturer,
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Model = item.Model,
                Odometer = item.Odometer,
                RepairHistories = singleLevel ? null : RepairHistory.ToListDTO(item.RepairHistories),
                User = singleLevel ? null : User.ToDTO(item.User),
                UserId = item.UserId,
                Year = item.Year

            };
        }

        public static ICollection<CarDTO> ToListDTO(ICollection<Car> list, bool singleLevel = true)
        {
            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public CarDTO ToDTO(bool singleLevel = true)
        {
            return new CarDTO
            {
                Make = Make,
                Manufacturer = Manufacturer,
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Model = Model,
                Odometer = Odometer,
                RepairHistories = singleLevel ? null : RepairHistory.ToListDTO(RepairHistories),
                User = singleLevel ? null : User.ToDTO(User),
                UserId = UserId,
                Year = Year

            };
        }
    }
}