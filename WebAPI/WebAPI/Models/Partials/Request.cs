using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;

namespace WebAPI.Models.ORM
{
    partial class Request
    {
        public static RequestDTO ToDTO(Request item, int level)
        {
            if (item == null || level <= 0)
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
                Shop = Shop.ToDTO(item.Shop, level - 1),
                User = User.ToDTO(item.User, level - 1),
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
                Car = Car.ToDTO(item.Car, level - 1),
                CarId = item.CarId,
                IssueDescription = item.IssueDescription,
                RepairDate = item.RepairDate,
                RequestDate = item.RequestDate,
                ShopAcceptedDate = item.ShopAcceptedDate,
                UserAcceptedDate = item.UserAcceptedDate
            };
        }

        public static ICollection<RequestDTO> ToListDTO(ICollection<Request> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public RequestDTO ToDTO(int level)
        {
            return level > 0 ? new RequestDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Shop = Shop.ToDTO(Shop, level - 1),
                User = User.ToDTO(User, level - 1),
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
                Car = Car.ToDTO(Car, level - 1),
                CarId = CarId,
                IssueDescription = IssueDescription,
                RepairDate = RepairDate,
                RequestDate = RequestDate,
                ShopAcceptedDate = ShopAcceptedDate,
                UserAcceptedDate = UserAcceptedDate
            } : null;
        }
    }
}