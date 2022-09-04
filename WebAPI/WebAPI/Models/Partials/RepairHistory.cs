using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{

    partial class RepairHistory
    {
        public static RepairHistoryDTO ToDTO(RepairHistory item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new RepairHistoryDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Shop = singleLevel ? null : Shop.ToDTO(item.Shop),
                User = singleLevel ? null : User.ToDTO(item.User),
                UserId = item.UserId,
                Car = singleLevel ? null : Car.ToDTO(item.Car),
                CarId = item.CarId,
                DateOfRepair = item.DateOfRepair,
                ShopId = item.ShopId
            };
        }

        public static ICollection<RepairHistoryDTO> ToListDTO(ICollection<RepairHistory> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public RepairHistoryDTO ToDTO(bool singleLevel = true)
        {
            return new RepairHistoryDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Shop = singleLevel ? null : Shop.ToDTO(Shop),
                User = singleLevel ? null : User.ToDTO(User),
                UserId = UserId,
                Car = singleLevel ? null : Car.ToDTO(Car),
                CarId = CarId,
                DateOfRepair = DateOfRepair,
                ShopId = ShopId
            };
        }
    }

}