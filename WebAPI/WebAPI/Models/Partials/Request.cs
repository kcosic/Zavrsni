using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Request
    {
        public static RequestDTO ToDTO(Request item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new RequestDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Shop = singleLevel ? null : Shop.ToDTO(item.Shop),
                User = singleLevel ? null : User.ToDTO(item.User),
                UserId = item.UserId,
                ShopId = item.ShopId,
                FinishDate = item.FinishDate,
                Price = item.Price,
                BillPicture = item.BillPicture,
                EstimatedRepairHours = item.EstimatedRepairHours,
                EstimatedPrice = item.EstimatedPrice,
                Completed = item.Completed,
                ShopAccepted = item.ShopAccepted,
                UserAccepted = item.UserAccepted,
                Car = singleLevel ? null : Car.ToDTO(item.Car),
                CarId = item.CarId
            };
        }

        public static ICollection<RequestDTO> ToListDTO(ICollection<Request> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public RequestDTO ToDTO(bool singleLevel = true)
        {
            return new RequestDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Shop = singleLevel ? null : Shop.ToDTO(Shop),
                User = singleLevel ? null : User.ToDTO(User),
                UserId = UserId,
                ShopId = ShopId,
                FinishDate = FinishDate,
                Price = Price,
                BillPicture = BillPicture,
                EstimatedRepairHours = EstimatedRepairHours,
                EstimatedPrice = EstimatedPrice,
                Completed = Completed,
                ShopAccepted = ShopAccepted,
                UserAccepted = UserAccepted,
                Car = singleLevel ? null : Car.ToDTO(Car),
                CarId = CarId
            };
        }
    }
}